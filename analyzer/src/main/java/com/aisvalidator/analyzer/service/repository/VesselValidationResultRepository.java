package com.aisvalidator.analyzer.service.repository;

import com.aisvalidator.analyzer.service.model.InterpolatedModel;
import com.aisvalidator.analyzer.service.model.VesselValidationResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface VesselValidationResultRepository extends MongoRepository<VesselValidationResult, String> {

}
