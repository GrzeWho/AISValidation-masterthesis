package com.aisvalidator.analyzer;

import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.rabbitmq.receiver.RabbitMqEventDispatcher;
import com.aisvalidator.rabbitmq.sender.RabbitMqEventPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Configuration
public class AnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyzerApplication.class, args);
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
