package com.aisvalidator.base.domain.model.event;

import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.base.domain.model.VesselValidationResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AISPositionValidatedEvent extends AISEvent {
  private long mmsi;
  private double longitude;
  private double latitude;
  private double predictedValue;
  private double actualValue;
  private double confidence;
  private List<String> rawNMEAMessages;
  private VesselValidationResult result;
}
