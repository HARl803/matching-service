package com.haribo.matching_service.mentee.domain;

import com.haribo.matching_service.global.enums.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reservation")
@Builder
@Getter
public class ReservationDocs {
    @Id
    private String id;
    private Integer reservationId;
    private MenteeAvailableTimes menteeAvailableTimes;
    private Log log;
    private Review review;

    @Getter
    @Builder
    public static class MenteeAvailableTimes {
        private String menteeId;
        private List<String> possibleStartTimes;
    }

    @Getter
    @Builder
    public static class Log {
        private String memberId;
        private ReservationStatus status;
        private LocalDateTime creationDate;
    }

    @Getter
    @Builder
    public static class Review {
        private String star;
        private String content;
        private LocalDateTime createdDate;
    }
}