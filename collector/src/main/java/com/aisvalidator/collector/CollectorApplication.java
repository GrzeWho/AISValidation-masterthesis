package com.aisvalidator.collector;

import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.rabbitmq.receiver.RabbitMqEventDispatcher;
import com.aisvalidator.rabbitmq.sender.RabbitMqEventPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class CollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectorApplication.class, args);
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
