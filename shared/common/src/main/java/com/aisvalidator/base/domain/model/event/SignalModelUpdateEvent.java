package com.aisvalidator.base.domain.model.event;

import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.base.domain.model.PositionMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.devtools.v85.systeminfo.model.VideoDecodeAcceleratorCapability;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignalModelUpdateEvent extends AISEvent {
  private double latitudeMinBound;
  private double latitudeMaxBound;
  private double longitudeMinBound;
  private double longitudeMaxBound;
  private int lengthInDays;

  public SignalModelUpdateEvent(double latitudeMinBound, double latitudeMaxBound, double longitudeMinBound, double longitudeMaxBound, int lengthInDays) {
    this.chainId = UUID.randomUUID().toString();
    this.timestamp = LocalDateTime.now();
    this.latitudeMinBound = latitudeMinBound;
    this.latitudeMaxBound = latitudeMaxBound;
    this.longitudeMinBound = longitudeMinBound;
    this.longitudeMaxBound = longitudeMaxBound;
    this.lengthInDays = lengthInDays;
  }
}
