package com.aisvalidator.analyzer.service;

import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.base.domain.model.event.SignalModelUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ModelService {
    private final EventPublisher publisher;

    @Value("${model.generate.latitude.lowerBound}")
    double latitudeLowerBound;
    @Value("${model.generate.latitude.upperBound}")
    double latitudeUpperBound;
    @Value("${model.generate.longitude.lowerBound}")
    double longitudeLowerBound;
    @Value("${model.generate.longitude.upperBound}")
    double longitudeUpperBound;
    @Value("${model.generate.lengthInDays}")
    int lengthInDays;

    public ModelService(EventPublisher publisher) {
        this.publisher = publisher;
    }
    @Scheduled(cron = "${model.generate.cron}")
    public void updateModel() {
        log.info("Model update triggered");
        this.publisher.publish(new SignalModelUpdateEvent(latitudeLowerBound, latitudeUpperBound, longitudeLowerBound, longitudeUpperBound, lengthInDays));
    }
}
