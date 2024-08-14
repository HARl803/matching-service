package com.haribo.matching_service.mentee.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "mentor_availability")
public class MentorAvailabilityDocs {
    @Id
    private String id;
    private String mentorId;
    private List<Availability> availability;
    private LocalDateTime lastUpdated;

    public static class Availability {
        private String dayOfWeek; // 'Monday', 'Tuesday'
        private List<TimeSlot> times;
    }

    public static class TimeSlot {
        private LocalDateTime startTime; // "HH:MM"
    }
}
