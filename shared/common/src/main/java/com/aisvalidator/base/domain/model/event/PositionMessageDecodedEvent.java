package com.aisvalidator.base.domain.model.event;

import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.base.domain.model.PositionMessage;
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
public class PositionMessageDecodedEvent extends AISEvent {
  private PositionMessage positionMessage;

  public PositionMessageDecodedEvent(PositionMessage positionMessage) {
    this.chainId = UUID.randomUUID().toString();
    this.timestamp = LocalDateTime.now();
    this.positionMessage = positionMessage;
  }
}
