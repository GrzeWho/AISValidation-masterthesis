
package com.aisvalidator.base.domain.model.importing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class MessageDetails {

    @JsonProperty("class")
    private String _class;
    @JsonProperty("device")
    private String device;
    @JsonProperty("rxtime")
    private String rxtime;
    @JsonProperty("channel")
    private String channel;
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lon")
    private Double longitude;
    @JsonProperty("nmea")
    private List<String> nmea = new ArrayList<String>();
    @JsonProperty("signalpower")
    private Double signalpower;
    @JsonProperty("ppm")
    private Double ppm;
    @JsonProperty("mmsi")
    private Long mmsi;

}
