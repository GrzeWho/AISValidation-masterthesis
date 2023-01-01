package com.aisvalidator.publisher.repository;

import com.aisvalidator.base.domain.model.ExternalVerificationResult;
import com.aisvalidator.base.domain.model.VesselValidationResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VesselStaticInfoValidationResultRepository extends MongoRepository<ExternalVerificationResult, String> {

    ExternalVerificationResult findBymmsi(Long mmsi);
}
