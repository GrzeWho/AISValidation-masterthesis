package com.aisvalidator.base.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString(callSuper = true)
public class VesselValidationResultWebInfo extends VesselValidationResult {
    private Integer imo;
    private String callSign;
    private String failureString;
    private String name;

    public VesselValidationResultWebInfo(VesselValidationResult result) {
        super.actualValue = result.getActualValue();
        super.confidence = result.getConfidence();
        super.actualValue = result.getActualValue();
        super.error = result.getError();
        super.mmsi = result.getMmsi();
        super.latitude = result.getLatitude();
        super.longitude = result.getLongitude();
        super.predictedValue = result.getPredictedValue();
        super.usedModelId =  result.getUsedModelId();
        super.lastUpdated = result.getLastUpdated();
        super.resultSummary = result.getResultSummary();
    }
}
