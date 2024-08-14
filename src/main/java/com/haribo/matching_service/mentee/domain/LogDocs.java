package com.haribo.matching_service.mentee.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "log")
public class LogDocs {
    @Id
    private String reservationId;
    private String memberId;
    // 예약대기, 시간선택, 결제, 채팅대기, 채팅입장, 매칭실패, 매칭완료
    private String status;
    private LocalDateTime creationDate;
}
