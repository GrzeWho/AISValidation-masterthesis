package com.aisvalidator.base.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public abstract class AISEvent {
  protected String id;
  protected String chainId;
  protected long mmsi;
  protected LocalDateTime timestamp;

  public AISEvent() {
    this.id = UUID.randomUUID().toString();
    this.timestamp = LocalDateTime.now();
  }
}
