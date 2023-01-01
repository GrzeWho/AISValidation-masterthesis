
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
public class Receiver {

    @JsonProperty("description")
    public String description;
    @JsonProperty("version")
    public Integer version;
    @JsonProperty("engine")
    public String engine;
    @JsonProperty("setting")
    public String setting;

}
