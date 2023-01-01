package com.aisvalidator.base.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ExternalVerificationFailReason {
    MMSI_NOT_UNIQUE,
    IMO_MISMATCH,
    NAME_MISMATCH,
    CALLSIGN_MISMATCH,
    MMSI_NOT_FOUND
}
