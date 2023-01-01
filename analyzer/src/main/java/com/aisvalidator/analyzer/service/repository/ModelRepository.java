package com.aisvalidator.analyzer.service.repository;

import com.aisvalidator.analyzer.service.model.InterpolatedModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelRepository extends MongoRepository<InterpolatedModel, String> {

    InterpolatedModel findFirstByOrderByIdDesc();

    public long count();
}
