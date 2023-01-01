package com.aisvalidator.analyzer.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Document("models")
public class InterpolatedModel {
    private String id;
    private LocalDateTime generatedAt;
    private double minlon;
    private double maxlon;
    private double minlat;
    private double maxlat;
    private double minVariance;
    private double maxVariance;
    private double[][] predictionGrid;
    private double[][] varianceGrid;
}
