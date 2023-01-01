package com.aisvalidator.decoder;

import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.rabbitmq.receiver.RabbitMqEventDispatcher;
import com.aisvalidator.rabbitmq.sender.RabbitMqEventPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DecoderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DecoderApplication.class, args);
	}

	@Bean
	public EventPublisher eventPublisher() {
		return new RabbitMqEventPublisher();
	}

	@Bean
	public EventDispatcher eventDispatcher() {
		return new RabbitMqEventDispatcher();
	}
}
