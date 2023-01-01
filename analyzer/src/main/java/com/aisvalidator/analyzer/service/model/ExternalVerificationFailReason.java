package com.aisvalidator.analyzer.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.EnumSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ExternalVerificationFailReason {
    MMSI_NOT_UNIQUE,
    IMO_MISMATCH,
    NAME_MISMATCH,
    CALLSIGN_MISMATCH,
}
