package com.haribo.matching_service.mentee.presentation.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MenteePossiRequest {
    private String mentorId;
    private List<String> possibleStartTime;
    private String symptomImageFile;
    private String symptomDetail;
}