
package com.aisvalidator.base.domain.model.importing;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class DataImport {

    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("stationid")
    private String stationId;
    @JsonProperty("encodetime")
    private String encodetime;
    @JsonProperty("receiver")
    private Receiver receiver;
    @JsonProperty("device")
    private Device device;
    @JsonProperty("msgs")
    private List<MessageDetails> messageDetails = new ArrayList<>();

}
