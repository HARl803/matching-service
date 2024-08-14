package com.haribo.matching_service.mentee.domain.repository;

import com.haribo.matching_service.mentee.domain.ReservationDocs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationDocsRepository extends MongoRepository<ReservationDocs, String> {
}
