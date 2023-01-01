package com.aisvalidator.publisher.repository;

import com.aisvalidator.base.domain.model.VesselValidationResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VesselValidationResultRepository extends MongoRepository<VesselValidationResult, String> {
    List<VesselValidationResult> findAll();

    VesselValidationResult findBymmsi(Long mmsi);

    @Query("{'lastUpdated': {$gte: ?0}}")
    List<VesselValidationResult> findAllNewerThan(LocalDateTime lastUpdated);
}
