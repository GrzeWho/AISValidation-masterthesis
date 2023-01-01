package com.aisvalidator.base.domain.repository;

import com.aisvalidator.base.domain.model.ExternalVesselResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExternalVesselResponseRepository extends MongoRepository<ExternalVesselResponse, String> {

    List<ExternalVesselResponse> findBymmsi(Long mmsi);
}
