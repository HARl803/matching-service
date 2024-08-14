package com.haribo.matching_service.mentee.domain.repository;

import com.haribo.matching_service.mentee.domain.MentorAvailabilityDocs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorAvailabilityDocsRepository extends MongoRepository<MentorAvailabilityDocs, String> {

}