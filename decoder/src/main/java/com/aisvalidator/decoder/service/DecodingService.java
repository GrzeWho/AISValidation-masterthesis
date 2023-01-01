package com.aisvalidator.decoder.service;

import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.base.domain.model.PositionMessage;
import com.aisvalidator.base.domain.model.VesselMessage;
import com.aisvalidator.base.domain.model.event.AISMessageReceivedEvent;
import com.aisvalidator.base.domain.model.event.PositionMessageDecodedEvent;
import com.aisvalidator.base.domain.model.event.VesselStaticMessageDecodedEvent;
import com.aisvalidator.base.domain.model.importing.MessageDetails;
import com.aisvalidator.decoder.repository.PositionMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.PositionReport;
import dk.tbsalling.aismessages.ais.messages.ShipAndVoyageData;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DecodingService {
    private final EventPublisher publisher;
    private final EventDispatcher dispatcher;
    protected ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private Environment env;

    @Autowired
    private PositionMessageRepository positionMessageRepository;

    public DecodingService(EventPublisher publisher, EventDispatcher dispatcher) {
        this.publisher = publisher;
        this.dispatcher = dispatcher;

        this.dispatcher.register(AISMessageReceivedEvent.class, e -> decodeMessage((AISMessageReceivedEvent) e));
    }

    public void decodeMessage(AISMessageReceivedEvent event) {
        MessageDetails messageDetails = event.getDetails();
        try {
            NMEAMessage[] messages = messageDetails.getNmea().stream()
                    .map(NMEAMessage::fromString)
                    .toArray(NMEAMessage[]::new);
            AISMessage decodedAIS = AISMessage.create(messages);
            if (decodedAIS.getMessageType().equals(AISMessageType.PositionReportClassAScheduled) || decodedAIS.getMessageType().equals(AISMessageType.PositionReportClassAAssignedSchedule) || decodedAIS.getMessageType().equals(AISMessageType.PositionReportClassAResponseToInterrogation)) {
                PositionReport positionReport = (PositionReport) decodedAIS;
                PositionMessage positionMessage = new PositionMessage();
                positionMessage.setLatitude(Double.valueOf(positionReport.getLatitude()));
                positionMessage.setLongitude(Double.valueOf(positionReport.getLongitude()));
                positionMessage.setStationId(event.getStationId());
                positionMessage.setEncodetime(event.getEncodeTime());
                positionMessage.setDecoderSetting(event.getDevice().getSetting());
                positionMessage.setMmsi(Long.valueOf(positionReport.getSourceMmsi().as9DigitString()));
                positionMessage.setSignalpower(messageDetails.getSignalpower());
                positionMessage.setCreatedAt(LocalDateTime.now());
                positionMessage.setNmea(messageDetails.getNmea());
                positionMessage.setMessageType(positionReport.getMessageType());
                positionMessage.setRepeatIndicator(positionReport.getRepeatIndicator());
                publisher.publish(new PositionMessageDecodedEvent(positionMessage));
                positionMessageRepository.save(positionMessage);
            } else if (decodedAIS.getMessageType().equals(AISMessageType.ShipAndVoyageRelatedData)) {
                ShipAndVoyageData shipAndVoyageData = (ShipAndVoyageData) decodedAIS;
                VesselMessage vesselMessage = new VesselMessage();
                vesselMessage.setCallSign(shipAndVoyageData.getCallsign());
                vesselMessage.setImo(shipAndVoyageData.getImo().getIMO());
                vesselMessage.setMmsi(Long.valueOf(shipAndVoyageData.getSourceMmsi().as9DigitString()));
                vesselMessage.setName(shipAndVoyageData.getShipName());
                publisher.publish(new VesselStaticMessageDecodedEvent(vesselMessage));
            }
        } catch (InvalidMessage | StringIndexOutOfBoundsException e) {
            log.warn("Invalid message from MMSI: {}. NMEA: {}. message = {}", messageDetails.getMmsi(),
                    messageDetails.getNmea().stream().map(String::toString).collect(Collectors.joining(", ")), e.getMessage());
        }

    }
}
