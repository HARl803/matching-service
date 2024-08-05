package com.haribo.matching_service.mentee.domain.repository;

import com.haribo.matching_service.mentee.domain.MentorAvailabilityDocs;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MentorAvailabilityRepository extends MongoRepository<MentorAvailabilityDocs, String> {

}