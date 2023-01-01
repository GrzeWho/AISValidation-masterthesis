package com.aisvalidator.analyzer.service.utils;

import com.aisvalidator.base.domain.model.ResultSummary;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AnalysisUtils {
    // Percentile point of 95% confidence interval
    private static final double PERCENTILE_POINT_95 = 1.96;
    // Percentile point of 99% confidence interval
    private static final double PERCENTILE_POINT_99 = 2.576;

    public static ResultSummary isWithinConfidence(double expectedValue, double variance, double actualValue) {
        double standardError = Math.sqrt(variance);
        double upperLimit = expectedValue + (standardError*PERCENTILE_POINT_95);
        double lowerLimit = expectedValue - (standardError*PERCENTILE_POINT_95);
        if (actualValue < lowerLimit || actualValue > upperLimit) {
            upperLimit = expectedValue + (standardError*PERCENTILE_POINT_99);
            lowerLimit = expectedValue - (standardError*PERCENTILE_POINT_99);
            if (actualValue < lowerLimit || actualValue > upperLimit) {
               return ResultSummary.VERY_LIKELY_SPOOFING;
            } else {
                return ResultSummary.POTENTIAL_SPOOFING;
            }
        }
        return ResultSummary.VALIDATED;
    }

    public static int calculateRelativeError(double receivedMeasurement, double predictedMeasurement) {
        return (int) (100*Math.abs((receivedMeasurement - predictedMeasurement)/receivedMeasurement));
    }

    public static Integer determineBin(double binStart, double binEnd, int binCount, double value) {
        if (value < binStart || value > binEnd) {
            return null;
        }
        double binSpaceWidth = Math.abs(binStart - binEnd);
        double binWidth = binSpaceWidth/binCount;
        double relativeValue = Math.abs(binStart - value);
        for (int i = 1; i <= binCount; i++) {
            if (i * binWidth >= relativeValue) {
                return i - 1;
            }
        }
        return null;
    }
}
