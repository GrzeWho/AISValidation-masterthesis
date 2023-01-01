package com.aisvalidator.analyzer.service.repository;

import com.aisvalidator.base.domain.model.ExternalVerificationResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VesselStaticInfoValidationResultRepository extends MongoRepository<ExternalVerificationResult, String> {

}
