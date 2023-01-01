package com.aisvalidator.publisher.service;

import com.aisvalidator.base.domain.contracts.EventDispatcher;
import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.base.domain.model.ExternalVerificationResult;
import com.aisvalidator.base.domain.model.VesselValidationResult;
import com.aisvalidator.base.domain.model.event.AISPositionValidatedEvent;
import com.aisvalidator.base.domain.model.event.MessagePublishedEvent;
import com.aisvalidator.base.domain.model.event.VesselStaticMessageValidatedEvent;
import com.aisvalidator.publisher.repository.VesselStaticInfoValidationResultRepository;
import com.aisvalidator.publisher.repository.VesselValidationResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.net.UnknownHostException;

@Service
@Slf4j
public class EventService {
    private final EventPublisher publisher;
    private final EventDispatcher dispatcher;
    @Autowired
    protected VesselValidationResultRepository vesselValidationResultRepository;
    @Autowired
    protected VesselStaticInfoValidationResultRepository vesselStaticInfoValidationResultRepository;
    @Autowired
    private UDPService udpService;

    public EventService(EventPublisher publisher, EventDispatcher dispatcher) {
        this.publisher = publisher;
        this.dispatcher = dispatcher;
        this.dispatcher.register(VesselStaticMessageValidatedEvent.class, e -> handleVesselStaticsValidatedEvent((VesselStaticMessageValidatedEvent) e));
        this.dispatcher.register(AISPositionValidatedEvent.class, e -> handleVesselPositionValidatedEvent((AISPositionValidatedEvent) e));}

    private void handleVesselPositionValidatedEvent(AISPositionValidatedEvent event) {
        vesselValidationResultRepository.save(event.getResult());
        log.info("Saved position validation: {}", event.getResult());
        MessagePublishedEvent publishedEvent = new MessagePublishedEvent(event.getResult());
        try {
            udpService.sendRawNMEAMessage(event.getRawNMEAMessages());
            publisher.publish(publishedEvent);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void handleVesselStaticsValidatedEvent(VesselStaticMessageValidatedEvent event) {
        ExternalVerificationResult externalVerificationResult = event.getExternalVerificationResult();
        log.info("Saved data validation {}", externalVerificationResult);
        vesselStaticInfoValidationResultRepository.save(externalVerificationResult);
        MessagePublishedEvent publishedEvent = new MessagePublishedEvent(externalVerificationResult);
        try {
            if (!event.getExternalVerificationResult().isFailure()) {
                udpService.sendRawNMEAMessage(event.getRawNMEAMessages());
            }
            publisher.publish(publishedEvent);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
