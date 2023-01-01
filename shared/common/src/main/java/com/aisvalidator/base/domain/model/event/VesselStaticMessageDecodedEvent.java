package com.aisvalidator.base.domain.model.event;

import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.base.domain.model.PositionMessage;
import com.aisvalidator.base.domain.model.VesselMessage;
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
public class VesselStaticMessageDecodedEvent extends AISEvent {
  private VesselMessage vesselMessage;

  public VesselStaticMessageDecodedEvent(VesselMessage vesselMessage) {
    this.chainId = UUID.randomUUID().toString();
    this.mmsi = vesselMessage.getMmsi();
    this.timestamp = LocalDateTime.now();
    this.vesselMessage = vesselMessage;
  }
}
