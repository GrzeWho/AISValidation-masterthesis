package com.aisvalidator.publisher.controllers;

import com.aisvalidator.base.domain.model.ExternalVerificationResult;
import com.aisvalidator.base.domain.model.VesselValidationResultWebInfo;
import com.aisvalidator.publisher.repository.VesselStaticInfoValidationResultRepository;
import com.aisvalidator.publisher.repository.VesselValidationResultRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/validated")
@Slf4j
public class PublishController {

    @Autowired
    public Environment env;

    @Autowired
    protected VesselValidationResultRepository vesselValidationResultRepository;

    @Autowired
    protected VesselStaticInfoValidationResultRepository vesselStaticInfoValidationResultRepository;

    protected ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("rawtypes")
    @GetMapping
    public List<VesselValidationResultWebInfo> requestData() {
        log.info("Called");
        return vesselValidationResultRepository.findAllNewerThan(LocalDateTime.now().minusHours(2)).stream().map(result -> {
            ExternalVerificationResult verificationResult = vesselStaticInfoValidationResultRepository.findBymmsi(result.getMmsi());
            VesselValidationResultWebInfo webInfo = new VesselValidationResultWebInfo(result);
            if (verificationResult != null) {
               webInfo.setCallSign(verificationResult.getCallSign());
               webInfo.setFailureString(verificationResult.getFailureString());
               webInfo.setName(verificationResult.getName());
               webInfo.setImo(verificationResult.getImo());
            }
            log.info(webInfo.toString());
            return webInfo;
        }).collect(Collectors.toList());
    }


}