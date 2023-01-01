
package com.aisvalidator.analyzer.service.model.external.datalastic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DatalasticVesselResponse {

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("name_ais")
    private String nameAis;
    @JsonProperty("mmsi")
    private String mmsi;
    @JsonProperty("imo")
    private String imo;
    @JsonProperty("eni")
    private Object eni;
    @JsonProperty("country_iso")
    private String countryIso;
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("callsign")
    private String callsign;
    @JsonProperty("type")
    private String type;
    @JsonProperty("type_specific")
    private String typeSpecific;
    @JsonProperty("gross_tonnage")
    private Integer grossTonnage;
    @JsonProperty("deadweight")
    private Integer deadweight;
    @JsonProperty("teu")
    private Object teu;
    @JsonProperty("liquid_gas")
    private Object liquidGas;
    @JsonProperty("length")
    private BigDecimal length;
    @JsonProperty("breadth")
    private BigDecimal breadth;
    @JsonProperty("draught_avg")
    private Object draughtAvg;
    @JsonProperty("draught_max")
    private Object draughtMax;
    @JsonProperty("speed_avg")
    private Object speedAvg;
    @JsonProperty("speed_max")
    private Object speedMax;
    @JsonProperty("year_built")
    private String yearBuilt;
    @JsonProperty("is_navaid")
    private Boolean isNavaid;
    @JsonProperty("home_port")
    private String homePort;
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
