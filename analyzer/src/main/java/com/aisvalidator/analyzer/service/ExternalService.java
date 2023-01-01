package com.aisvalidator.analyzer.service;

import com.aisvalidator.analyzer.service.model.external.ExternalProviderProperties;
import com.aisvalidator.analyzer.service.model.external.VesselInfoProvider;
import com.aisvalidator.analyzer.service.model.external.datalastic.DatalasticResponse;
import com.aisvalidator.analyzer.service.model.external.datalastic.DatalasticVesselResponse;
import com.aisvalidator.analyzer.service.model.external.marinetraffic.MarineTrafficVesselResponse;
import com.aisvalidator.base.domain.model.DataSource;
import com.aisvalidator.base.domain.model.ExternalVesselResponse;
import com.aisvalidator.base.domain.model.VesselMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExternalService {
    @Autowired
    private ExternalProviderProperties datalasticConfig;
    @Autowired
    private ExternalProviderProperties marineTrafficConfig;

    @Autowired
    private VesselInfoProvider<DatalasticResponse> datalasticVesselProvider;

    @Autowired
    private VesselInfoProvider<MarineTrafficVesselResponse> marineTrafficVesselProvider;

    public List<ExternalVesselResponse> verifyShipInfo(VesselMessage vesselMessage) {
        try {
            return collectData(String.valueOf(vesselMessage.getMmsi()));
        } catch (IOException | InterruptedException e) {
            return List.of();
        }
    }

    private List<ExternalVesselResponse> collectData(String mmsi) throws IOException, InterruptedException {
        List<ExternalVesselResponse> responseList = new ArrayList<>();
        if (datalasticConfig.isEnabled()) {
            DatalasticResponse response = datalasticVesselProvider.verify(datalasticConfig, mmsi, DatalasticResponse.class);
            List<ExternalVesselResponse> mappedResponses = response.getData().stream().map(this::mapFromDatalasticResponse).collect(Collectors.toList());
            responseList.addAll(mappedResponses);
        } else if (marineTrafficConfig.isEnabled()) {
            MarineTrafficVesselResponse response = marineTrafficVesselProvider.verify(marineTrafficConfig, mmsi, MarineTrafficVesselResponse.class);
            ExternalVesselResponse mappedResponse = this.mapFromMarineTrafficResponse(response);
            responseList.add(mappedResponse);
        }
        return responseList;
    }

    private ExternalVesselResponse mapFromDatalasticResponse(DatalasticVesselResponse response) {
        ExternalVesselResponse externalVesselResponse = new ExternalVesselResponse();
        externalVesselResponse.setMmsi(Long.parseLong(response.getMmsi()));
        if (!StringUtils.isAllEmpty(response.getImo())) {
            externalVesselResponse.setImo(Integer.parseInt(response.getImo()));
        } else {
            externalVesselResponse.setImo(0);
        }
        externalVesselResponse.setLength(response.getLength());
        externalVesselResponse.setBreadth(response.getBreadth());
        externalVesselResponse.setGrossTonnage(response.getGrossTonnage());
        externalVesselResponse.setName(response.getName());
        externalVesselResponse.setCallSign(response.getCallsign());
        externalVesselResponse.setSource(DataSource.DATALASTIC);
        return externalVesselResponse;
    }
    private ExternalVesselResponse mapFromMarineTrafficResponse(MarineTrafficVesselResponse response) {
        ExternalVesselResponse externalVesselResponse = new ExternalVesselResponse();
        externalVesselResponse.setMmsi(Long.parseLong(response.getMmsi()));
        if (StringUtils.isAllEmpty(response.getImo())) {
            externalVesselResponse.setImo(Integer.parseInt(response.getImo()));
        } else {
            externalVesselResponse.setImo(0);
        }
        externalVesselResponse.setLength(new BigDecimal(response.getLength()));
        externalVesselResponse.setBreadth(new BigDecimal(response.getWidth()));
        externalVesselResponse.setGrossTonnage(Integer.parseInt(response.getGrt()));
        externalVesselResponse.setName(response.getShipname());
        externalVesselResponse.setCallSign(response.getCallsign());
        externalVesselResponse.setSource(DataSource.MARINE_TRAFFIC);
        return externalVesselResponse;
    }
}
