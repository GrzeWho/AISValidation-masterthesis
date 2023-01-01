package com.aisvalidator.base.domain.contracts;

import com.aisvalidator.base.domain.model.AISEvent;

public interface EventPublisher {
  <T extends AISEvent> void publish(T event);
}
