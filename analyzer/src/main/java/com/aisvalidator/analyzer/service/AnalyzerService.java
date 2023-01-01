package com.aisvalidator.analyzer.service;

import com.aisvalidator.analyzer.service.model.InterpolatedModel;
import com.aisvalidator.base.domain.model.ResultSummary;
import com.aisvalidator.analyzer.service.repository.ExternalVesselResponseRepository;
import com.aisvalidator.analyzer.service.repository.ModelRepository;
import com.aisvalidator.analyzer.service.repository.VesselStaticInfoValidationResultRepository;
import com.aisvalidator.analyzer.service.repository.VesselValidationResultRepository;
import com.aisvalidator.analyzer.service.utils.AnalysisUtils;
import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.base.domain.model.ExternalVerificationFailReason;
import com.aisvalidator.base.domain.model.ExternalVerificationResult;
import com.aisvalidator.base.domain.model.ExternalVesselResponse;
import com.aisvalidator.base.domain.model.PositionMessage;
import com.aisvalidator.base.domain.model.VesselValidationResult;
import com.aisvalidator.base.domain.model.event.AISPositionValidatedEvent;
import com.aisvalidator.base.domain.model.event.PositionMessageDecodedEvent;
import com.aisvalidator.base.domain.model.event.VesselStaticMessageDecodedEvent;
import com.aisvalidator.base.domain.model.event.VesselStaticMessageValidatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnalyzerService {
    private EventPublisher publisher;
    private EventDispatcher dispatcher;

    private ModelRepository modelRepository;

    @Autowired
    private ExternalVesselResponseRepository externalVesselResponseRepository;

    @Autowired
    private VesselValidationResultRepository vesselValidationResultRepository;

    @Autowired
    private VesselStaticInfoValidationResultRepository vesselStaticInfoValidationResultRepository;

    @Autowired
    private ExternalService externalService;

    public AnalyzerService(EventPublisher publisher, EventDispatcher dispatcher, ModelRepository modelRepository) {
        this.publisher = publisher;
        this.dispatcher = dispatcher;
        this.modelRepository = modelRepository;
        this.dispatcher.register(VesselStaticMessageDecodedEvent.class, e -> analyzeVesselStaticPositionReport((VesselStaticMessageDecodedEvent) e));
        this.dispatcher.register(PositionMessageDecodedEvent.class, e -> analyzeMessage((PositionMessageDecodedEvent) e));}

    public void analyzeMessage(PositionMessageDecodedEvent event) {
        PositionMessage info = event.getPositionMessage();
        InterpolatedModel model = Optional.ofNullable(modelRepository.findFirstByOrderByIdDesc()).orElseThrow(() -> new RuntimeException("No models available"));
        if (info.getLatitude() > model.getMaxlat() || info.getLatitude() < model.getMinlat()) {
            log.info("Vessel outside of model bounds: lat: {}, modelLatMin: {}, modelLatMax: {}", info.getLatitude(), model.getMinlat(), model.getMaxlat());
            return;
        } else if (info.getLongitude() > model.getMaxlon() || info.getLongitude() < model.getMinlon()) {
            log.info("Vessel outside of model bounds: lon: {}, modelLonMin: {}, modelLonMax: {}", info.getLongitude(), model.getMinlon(), model.getMaxlon());
            return;
        }
        int lonBins = model.getPredictionGrid()[0].length;
        int latBins = model.getPredictionGrid().length;
        int lonBin = Optional.ofNullable(AnalysisUtils.determineBin(model.getMinlon(), model.getMaxlon(), lonBins, info.getLongitude())).orElseThrow(() -> new RuntimeException("Longitude bin not found for model fitting the position" + event.getPositionMessage()));
        int latBin = Optional.ofNullable(AnalysisUtils.determineBin(model.getMinlat(), model.getMaxlat(), latBins, info.getLatitude())).orElseThrow(() -> new RuntimeException("Latitude bin not found for model fitting the position" + event.getPositionMessage()));
        double predictedSignal = model.getPredictionGrid()[latBin][lonBin];
        ResultSummary resultSummary = AnalysisUtils.isWithinConfidence(predictedSignal, model.getVarianceGrid()[latBin][lonBin], event.getPositionMessage().getSignalpower());
        if (resultSummary != ResultSummary.VALIDATED) {
            log.info("not valid: {} predicted: {}, actual: {}, variance: {}", event.getPositionMessage().getMmsi(), predictedSignal, event.getPositionMessage().getSignalpower(), model.getVarianceGrid()[latBin][lonBin]);
        }
        AISPositionValidatedEvent publishEvent = new AISPositionValidatedEvent();
        VesselValidationResult vesselValidationResult = new VesselValidationResult();
        vesselValidationResult.setMmsi(event.getPositionMessage().getMmsi());
        vesselValidationResult.setActualValue(event.getPositionMessage().getSignalpower());
        vesselValidationResult.setLatitude(info.getLatitude());
        vesselValidationResult.setLongitude(info.getLongitude());
        vesselValidationResult.setResultSummary(resultSummary);
        vesselValidationResult.setPredictedValue(predictedSignal);
        vesselValidationResult.setError(Math.abs(event.getPositionMessage().getSignalpower() - predictedSignal));
        vesselValidationResult.setUsedModelId(model.getId());
        vesselValidationResult.setLastUpdated(LocalDateTime.now());
        publishEvent.setRawNMEAMessages(info.getNmea());
        publishEvent.setResult(vesselValidationResult);
        log.info("Succesfully validated.");
        this.publisher.publish(publishEvent);
    }

    public void analyzeVesselStaticPositionReport(VesselStaticMessageDecodedEvent event) {
        ExternalVerificationResult externalVerificationResult = new ExternalVerificationResult();
        List<ExternalVesselResponse> externalVesselInfoList = externalVesselResponseRepository.findBymmsi(event.getMmsi());
        externalVerificationResult.setName(event.getVesselMessage().getName());
        externalVerificationResult.setMmsi(event.getMmsi());
        externalVerificationResult.setImo(event.getVesselMessage().getImo());
        externalVerificationResult.setCallSign(event.getVesselMessage().getCallSign());
        externalVerificationResult.setLastUpdated(LocalDateTime.now());
        if (externalVesselInfoList.size() == 0) {
            log.info("Have not found info vessel with MMSI: {} in the database", event.getMmsi());
            externalVesselInfoList = externalService.verifyShipInfo(event.getVesselMessage());
            externalVesselResponseRepository.saveAll(externalVesselInfoList);
        }
        if (externalVesselInfoList.size() > 1) {
            log.info("More than one vessel matching the MMSI: {} found. Failure", event.getMmsi());
            externalVerificationResult.setFailureString(String.format("More than one vessel matching the MMSI: %s found. Known names: %s", event.getMmsi(), externalVesselInfoList.stream().map(ExternalVesselResponse::getName).collect(Collectors.joining(", "))));
            externalVerificationResult.setFailure(true);
            externalVerificationResult.setFailReasons(EnumSet.of(ExternalVerificationFailReason.MMSI_NOT_UNIQUE));
        }
        if (externalVesselInfoList.size() == 1) {
            log.info("Event: {}, Received: {}", event, externalVesselInfoList.get(0));
            if (!event.getVesselMessage().getCallSign().equals(externalVesselInfoList.get(0).getCallSign())) {
                externalVerificationResult.setFailure(true);
                externalVerificationResult.setFailureString(String.format("Incorrect callsign. Received: %s, External: %s", event.getVesselMessage().getCallSign(), externalVesselInfoList.get(0).getCallSign()));
                externalVerificationResult.setFailReasons(EnumSet.of(ExternalVerificationFailReason.CALLSIGN_MISMATCH));
            }
            if (!event.getVesselMessage().getImo().equals(externalVesselInfoList.get(0).getImo())) {
                externalVerificationResult.setFailure(true);
                externalVerificationResult.setFailureString(String.format("Incorrect IMO. Received: %s, External: %s", event.getVesselMessage().getImo(), externalVesselInfoList.get(0).getImo()));
                externalVerificationResult.setFailReasons(EnumSet.of(ExternalVerificationFailReason.IMO_MISMATCH));
            }
            if (!event.getVesselMessage().getName().equals(externalVesselInfoList.get(0).getName())) {
                externalVerificationResult.setFailure(true);
                externalVerificationResult.setFailureString(String.format("Incorrect name. Received: %s, External: %s", event.getVesselMessage().getName(), externalVesselInfoList.get(0).getName()));
                externalVerificationResult.setFailReasons(EnumSet.of(ExternalVerificationFailReason.NAME_MISMATCH));
            }
        } else if (externalVesselInfoList.size() == 0) {
            externalVerificationResult.setFailure(true);
            externalVerificationResult.setFailureString(String.format("Vessel MMSI not found in external database. Received: %s", event.getVesselMessage().getMmsi()));
            externalVerificationResult.setFailReasons(EnumSet.of(ExternalVerificationFailReason.MMSI_NOT_FOUND));
        }
        VesselStaticMessageValidatedEvent sendEvent = new VesselStaticMessageValidatedEvent(externalVerificationResult, event.getVesselMessage().getNmea());
        sendEvent.setMmsi(event.getMmsi());
        sendEvent.setRawNMEAMessages(event.getVesselMessage().getNmea());

        this.publisher.publish(sendEvent);
    }
}
