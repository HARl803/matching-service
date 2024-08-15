package com.haribo.matching_service.mento.presentation.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MatchingConfirmedRequest {
    @JsonProperty("matching_id")
    String matchingId;

    @JsonProperty("possible_start_time")
    String possibleStartTime;
}
