package com.aisvalidator.rabbitmq.receiver;

import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.model.AISEvent;
import com.aisvalidator.rabbitmq.configuration.RabbitMqBased;
import com.aisvalidator.rabbitmq.configuration.RabbitMqTenantConfig;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class RabbitMqEventDispatcher extends RabbitMqBased implements EventDispatcher {
  @Override public void registerSpecific(Class<? extends AISEvent> clazz) {
    try {
      String queue = channel.queueDeclare("ais-validator-" + clazz.getSimpleName() + UUID.randomUUID(), false, false, true, null).getQueue();
      channel.queueBind(queue, RabbitMqTenantConfig.EXCHANGE, clazz.getSimpleName());
      DeliverCallback deliverCallback = (tag, msg) -> {
        try {
          dispatch(clazz, mapper.readValue(msg.getBody(), clazz));
        } catch (JsonParseException | JsonMappingException je) {
          log.error("Could not parse from Rabbit MQ queue. Error: {}", je.getMessage());
        }
      };
      channel.basicConsume(queue, true, deliverCallback, cancelCallback -> {});
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
