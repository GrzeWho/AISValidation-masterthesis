package com.aisvalidator.analyzer.service.model.external.marinetraffic;

import com.aisvalidator.analyzer.service.model.external.ProviderVesselResponse;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class MarineTrafficVesselResponse extends ProviderVesselResponse {

    @JsonProperty("MMSI")
    private String mmsi;
    @JsonProperty("LAT")
    private String lat;
    @JsonProperty("LON")
    private String lon;
    @JsonProperty("SPEED")
    private String speed;
    @JsonProperty("COURSE")
    private String course;
    @JsonProperty("TIMESTAMP")
    private String timestamp;
    @JsonProperty("SHIPNAME")
    private String shipname;
    @JsonProperty("SHIPTYPE")
    private String shiptype;
    @JsonProperty("IMO")
    private String imo;
    @JsonProperty("CALLSIGN")
    private String callsign;
    @JsonProperty("FLAG")
    private String flag;
    @JsonProperty("CURRENT_PORT")
    private String currentPort;
    @JsonProperty("LAST_PORT")
    private String lastPort;
    @JsonProperty("LAST_PORT_TIME")
    private String lastPortTime;
    @JsonProperty("DESTINATION")
    private String destination;
    @JsonProperty("ETA")
    private String eta;
    @JsonProperty("LENGTH")
    private String length;
    @JsonProperty("WIDTH")
    private String width;
    @JsonProperty("DRAUGHT")
    private String draught;
    @JsonProperty("GRT")
    private String grt;
    @JsonProperty("DWT")
    private String dwt;
    @JsonProperty("YEAR_BUILT")
    private String yearBuilt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}