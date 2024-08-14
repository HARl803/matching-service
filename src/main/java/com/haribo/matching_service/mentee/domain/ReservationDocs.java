package com.haribo.matching_service.mentee.domain;

import com.haribo.matching_service.global.enums.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Document(collection = "reservation")
public class ReservationDocs {
    @Override
    public String toString() {
        return "ReservationDocs{" +
                "id='" + id + '\'' +
                ", reservationId=" + reservationId +
                ", menteeAvailableTimes=" + menteeAvailableTimes +
                ", log=" + log +
                ", review=" + review +
                '}';
    }

    @Id
    private String id;

    @Field("reservation_id")
    private Integer reservationId;

    @Field("mentee_available_times")
    private Map<String,MenteeAvailableTimes> menteeAvailableTimes;

    private Map<String,Log> log;
    private Review review;

    @Getter
    @Builder
    public static class MenteeAvailableTimes {

        @Field("mentee_id")
        private String menteeId;

        @Field("possible_start_times")
        private List<String> possibleStartTimes;

        @Override
        public String toString() {
            return "MenteeAvailableTimes{" +
                    "menteeId='" + menteeId + '\'' +
                    ", possibleStartTimes=" + possibleStartTimes +
                    '}';
        }
    }

    @Getter
    @Builder
    public static class Log {
//        @Field("num")
//        private String num;

        @Field("member_id")
        private String memberId;

        private String status;

        @Field("creation_date")
        private LocalDateTime creationDate;

        public static Log from(ReservationStatus status, String memberId, LocalDateTime creationDate) {
            return Log.builder()
                    .status(status.getKorean())
                    .memberId(memberId)
                    .creationDate(creationDate)
                    .build();
        }

        @Override
        public String toString() {
            return "Log{" +
                    "memberId='" + memberId + '\'' +
                    ", status=" + status +
                    ", creationDate=" + creationDate +
                    '}';
        }
    }

    @Getter
    @Builder
    public static class Review {
        private String star;
        private String content;

        @Field("creation_date")
        private LocalDateTime createdDate;

        @Override
        public String toString() {
            return "Review{" +
                    "star='" + star + '\'' +
                    ", content='" + content + '\'' +
                    ", createdDate=" + createdDate +
                    '}';
        }
    }
}