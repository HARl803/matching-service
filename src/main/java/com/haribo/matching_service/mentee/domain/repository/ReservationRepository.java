package com.haribo.matching_service.mentee.domain.repository;

import com.haribo.matching_service.mentee.domain.ReservationDocs;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepository extends MongoRepository<ReservationDocs, String> {

}
