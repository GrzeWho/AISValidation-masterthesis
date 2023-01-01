package com.aisvalidator.base.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Document("vesselinfo")
public class VesselMessage extends DecodedMessage {
    private String callSign;
    private Integer imo;
    private String name;
}
