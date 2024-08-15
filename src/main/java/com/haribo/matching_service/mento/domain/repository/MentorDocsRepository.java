package com.haribo.matching_service.mento.domain.repository;

import com.haribo.matching_service.mento.domain.MentorDocs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorDocsRepository extends MongoRepository<MentorDocs, String> {
    MentorDocs findByProfileId(String profileId);
}