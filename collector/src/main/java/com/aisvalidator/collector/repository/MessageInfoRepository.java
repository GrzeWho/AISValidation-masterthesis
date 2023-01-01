package com.aisvalidator.collector.repository;

import com.aisvalidator.base.domain.model.PositionMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageInfoRepository extends MongoRepository<PositionMessage, String> {

    @Query("{mmsi:'?0'}")
    List<PositionMessage> findItemByMMSI(String mmsi);

}
