package com.haribo.matching_service.mento.application.service;

import com.haribo.matching_service.mento.presentation.request.MatchingConfirmedRequest;
import com.haribo.matching_service.mento.presentation.response.MemberIdResponse;
import com.haribo.matching_service.mento.presentation.response.MentorPossiTimesResponse;

import java.util.List;

public interface MentorService {
    List<Long> getMentorPossiTime(String mentorId);

    void fixMentorPossiTime(MatchingConfirmedRequest matchingConfirmedRequest);

    MemberIdResponse getMemberId(String matchingId);
}
