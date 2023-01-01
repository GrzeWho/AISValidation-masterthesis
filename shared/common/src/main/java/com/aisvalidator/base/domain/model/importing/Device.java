
package com.aisvalidator.base.domain.model.importing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class Device {

    @JsonProperty("product")
    private String product;
    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("serial")
    private String serial;
    @JsonProperty("setting")
    private String setting;

}
