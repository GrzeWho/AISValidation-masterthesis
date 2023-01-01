package com.aisvalidator.base.domain.contracts;

import com.aisvalidator.base.domain.model.AISEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface EventDispatcher {
  Map<Class<? extends AISEvent>, List<EventConsumer<? extends AISEvent>>> consumers = new ConcurrentHashMap<>();

  default void register(Class<? extends AISEvent> clazz, EventConsumer<? extends AISEvent> consumer) {
    if (!consumers.containsKey(clazz)) {
      registerSpecific(clazz);
      consumers.put(clazz, new LinkedList<>());
    }
    consumers.get(clazz).add(consumer);
  }

  void registerSpecific(Class<? extends AISEvent> clazz);

  default void dispatch(Class<? extends AISEvent> clazz, AISEvent event) {
    for (EventConsumer consumer : consumers.get(clazz)) {
      consumer.accept(event);
    }
  }
}
