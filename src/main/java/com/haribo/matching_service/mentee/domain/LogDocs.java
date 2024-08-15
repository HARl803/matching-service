package com.haribo.matching_service.mentee.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "log")
public class LogDocs {
    @Id
    private String reservationId;
    private String memberId;
    private String status;
    private LocalDateTime creationDate;
}
