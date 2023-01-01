
package com.aisvalidator.base.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Document("positionmessage")
@NoArgsConstructor
public class PositionMessage extends DecodedMessage {

    private LocalDateTime createdAt;

    private Integer repeatIndicator;

    private String encodetime;

    private String decoderSetting;

    private String stationId;

    private Double signalpower;

    private Double latitude;

    private Double longitude;

}
