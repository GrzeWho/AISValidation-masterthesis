package com.aisvalidator.rabbitmq.sender;

import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.rabbitmq.configuration.RabbitMqBased;
import com.aisvalidator.rabbitmq.configuration.RabbitMqTenantConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RabbitMqEventPublisher extends RabbitMqBased implements EventPublisher {
  @Override public <T extends AISEvent> void publish(T event) {
      try {
        channel.basicPublish(RabbitMqTenantConfig.EXCHANGE, event.getClass().getSimpleName(), null, mapper.writeValueAsBytes(event));
      } catch (JsonProcessingException e) {
        log.error("Could not send {} event using Rabbit MQ. Event payload could not be processed. Message: {}", event, e.getMessage());
      } catch (IOException e) {
        log.error("Could not send {} event using Rabbit MQ. Message could not be queued. Message: {}", event, e.getMessage());
      }
  }
}
