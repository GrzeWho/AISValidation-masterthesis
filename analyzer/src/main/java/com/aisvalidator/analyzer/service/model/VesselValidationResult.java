package com.aisvalidator.analyzer.service.model;

import com.aisvalidator.base.domain.model.ResultSummary;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Document("vesselvalidationresult")
public class VesselValidationResult {
    private String id;
    private long mmsi;
    private double longitude;
    private double latitude;
    private double predictedValue;
    private double actualValue;
    private double error;
    private ResultSummary valid;
    private int relativeError;
    private String usedModelId;
}

