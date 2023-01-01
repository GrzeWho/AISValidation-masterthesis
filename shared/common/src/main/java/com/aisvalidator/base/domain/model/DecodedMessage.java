package com.aisvalidator.base.domain.model;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter @ToString
public abstract class DecodedMessage {
  @Id
  protected String id;
  protected Long mmsi;
  protected LocalDateTime timestamp;
  protected AISMessageType messageType;
  protected List<String> nmea;
  public DecodedMessage() {
    this.timestamp = LocalDateTime.now();
  }
}
