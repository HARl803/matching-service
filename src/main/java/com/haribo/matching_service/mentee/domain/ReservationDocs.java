package com.haribo.matching_service.mentee.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "reservation")
public class ReservationDocs {

    @Id
    private String reservationId;
    private Mento mentoAvailable;
    private Mentee menteeAvailable;

    public static class Mento {
        private String mentoId;
        private List<String> possibleStartTimes;
    }

    public static class Mentee {
        private String menteeId;
        private List<String> possibleStartTimes;
        private String pcId;
        private String symptom;
    }
}