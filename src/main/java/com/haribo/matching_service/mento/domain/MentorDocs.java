package com.haribo.matching_service.mento.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mento")
public class MentorDocs {
    @Id
    private String id;

    @Field("profile_id")
    private String profileId;

    private Integer totalCnt;

    private Integer matchingRate;

    private Integer star;

    private List<Long> times;

    private List<Integer> techs;

    private List<String> questions;

    @Field("reservation_record")
    private List<String> reservationRecord;
}