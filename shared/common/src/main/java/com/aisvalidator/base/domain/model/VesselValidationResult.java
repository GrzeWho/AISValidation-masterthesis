package com.aisvalidator.base.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Document("vesselvalidationresult")
public class VesselValidationResult {
    @Id
    protected long mmsi;
    protected double longitude;
    protected double latitude;
    protected double predictedValue;
    protected double actualValue;
    protected double error;
    protected double confidence;
    protected ResultSummary resultSummary;
    protected String usedModelId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime lastUpdated;
}
