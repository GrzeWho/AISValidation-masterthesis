package com.aisvalidator.base.domain.repository;

import com.aisvalidator.base.domain.model.VesselValidationResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VesselValidationResultRepository extends MongoRepository<VesselValidationResult, String> {
    List<VesselValidationResult> findAll();
}
