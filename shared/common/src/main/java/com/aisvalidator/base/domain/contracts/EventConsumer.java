package com.aisvalidator.base.domain.contracts;

import com.aisvalidator.base.domain.model.AISEvent;

@FunctionalInterface
public interface EventConsumer<T extends AISEvent> {
  void accept(T event);
}
