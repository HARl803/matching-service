package com.haribo.matching_service.mento.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class MemberIdResponse {
    String mentorId;
    String menteeId;
}
