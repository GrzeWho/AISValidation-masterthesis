package com.aisvalidator.analyzer.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.EnumSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Document("externalverificationresult")
public class ExternalVerificationResult {
    private long mmsi;
    private boolean failure;
    private EnumSet<ExternalVerificationFailReason> failReasons;
}