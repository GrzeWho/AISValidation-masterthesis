package com.aisvalidator.base.domain.model.event;

import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.base.domain.model.importing.Device;
import com.aisvalidator.base.domain.model.importing.MessageDetails;
import com.aisvalidator.base.domain.model.importing.Receiver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AISMessageReceivedEvent extends AISEvent {
  private MessageDetails details;
  private String stationId;
  private Receiver receiver;
  private Device device;
  private String encodeTime;

  public AISMessageReceivedEvent(MessageDetails details, String stationId, Receiver receiver, Device device, String encodeTime) {
    this.chainId = UUID.randomUUID().toString();
    this.timestamp = LocalDateTime.now();
    this.details = details;
    this.stationId = stationId;
    this.receiver = receiver;
    this.device = device;
    this.encodeTime = encodeTime;
  }
}
