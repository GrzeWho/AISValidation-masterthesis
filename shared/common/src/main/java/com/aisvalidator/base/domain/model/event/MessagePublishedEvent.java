package com.aisvalidator.base.domain.model.event;

import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.base.domain.model.ExternalVerificationResult;
import com.aisvalidator.base.domain.model.VesselValidationResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessagePublishedEvent extends AISEvent {
  private VesselValidationResult validationResult;
  private ExternalVerificationResult externalVerificationResult;

  public MessagePublishedEvent(VesselValidationResult result) {
    this.mmsi = result.getMmsi();
    this.validationResult = result;
  }

  public MessagePublishedEvent(ExternalVerificationResult result) {
   this.mmsi = result.getMmsi();
   this.externalVerificationResult = result;
  }
}
