package com.aisvalidator.rabbitmq.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class RabbitMqBased {
  protected Channel channel;
  protected ObjectMapper mapper;

  public RabbitMqBased(){
    try {
      final ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(RabbitMqTenantConfig.HOST);
      final Connection connection = factory.newConnection();
      this.channel = connection.createChannel();
      this.channel.exchangeDeclare(RabbitMqTenantConfig.EXCHANGE, BuiltinExchangeType.DIRECT, true, true, null);
      this.mapper = new ObjectMapper();
      this.mapper.findAndRegisterModules();
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }
}
