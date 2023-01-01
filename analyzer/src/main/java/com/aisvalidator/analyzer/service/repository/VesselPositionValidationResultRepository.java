package com.aisvalidator.analyzer.service.repository;

import com.aisvalidator.analyzer.service.model.VesselValidationResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VesselPositionValidationResultRepository extends MongoRepository<VesselValidationResult, String> {

}
