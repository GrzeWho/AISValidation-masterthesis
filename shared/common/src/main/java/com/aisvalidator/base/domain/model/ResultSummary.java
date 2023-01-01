package com.aisvalidator.base.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResultSummary {
    VALIDATED("Vessel validated successfully"), // 95% of sample
    POTENTIAL_SPOOFING("Potential spoofing"), // 95% to 99% of sample
    VERY_LIKELY_SPOOFING("Very likely spoofing"); // above 99% of sample

    private final String description;
}
