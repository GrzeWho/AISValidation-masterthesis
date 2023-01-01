package com.aisvalidator.publisher;

import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.rabbitmq.receiver.RabbitMqEventDispatcher;
import com.aisvalidator.rabbitmq.sender.RabbitMqEventPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
public class PublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublisherApplication.class, args);
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
