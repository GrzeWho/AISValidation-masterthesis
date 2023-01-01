package com.aisvalidator.base.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Document("externalvesselresponses")
public class ExternalVesselResponse {

    private String id;
    private Long mmsi;
    private Integer imo;
    private String name;
    private String callSign;
    private DataSource source;
    private BigDecimal length;
    private BigDecimal breadth;
    private Integer grossTonnage;
}
