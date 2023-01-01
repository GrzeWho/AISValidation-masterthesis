package com.aisvalidator.collector.controllers;

import com.aisvalidator.base.domain.contracts.EventPublisher;
import com.aisvalidator.base.domain.model.event.AISMessageReceivedEvent;
import com.aisvalidator.base.domain.model.importing.DataImport;
import com.aisvalidator.collector.repository.MessageInfoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/collect")
@Slf4j
public class CollectController {

    @Autowired
    public Environment env;

    private final EventPublisher publisher;

    protected ObjectMapper mapper = new ObjectMapper();

    public CollectController(EventPublisher publisher) {
        this.publisher = publisher;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity collectData(@RequestBody DataImport body) {
        log.info(body.toString());
        body.getMessageDetails().forEach(messageDetails -> {
            AISMessageReceivedEvent event = new AISMessageReceivedEvent(messageDetails, body.getStationId(), body.getReceiver(), body.getDevice(),
                    body.getEncodetime());
            publisher.publish(event);
        });
        log.info("Created an event");
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


}