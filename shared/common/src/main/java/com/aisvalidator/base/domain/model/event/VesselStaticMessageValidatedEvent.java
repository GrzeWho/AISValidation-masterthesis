package com.aisvalidator.base.domain.model.event;

import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.base.domain.model.ExternalVerificationResult;
import com.aisvalidator.base.domain.model.VesselMessage;
import com.aisvalidator.base.domain.model.VesselValidationResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VesselStaticMessageValidatedEvent extends AISEvent {
  private ExternalVerificationResult externalVerificationResult;
  private List<String> rawNMEAMessages;

  public VesselStaticMessageValidatedEvent(ExternalVerificationResult externalVerificationResult, List<String> rawNMEAMessages) {
    this.chainId = UUID.randomUUID().toString();
    this.mmsi = externalVerificationResult.getMmsi();
    this.timestamp = LocalDateTime.now();
    this.externalVerificationResult = externalVerificationResult;
    this.rawNMEAMessages = rawNMEAMessages;
  }
}
