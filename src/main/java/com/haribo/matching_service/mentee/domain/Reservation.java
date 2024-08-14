package com.haribo.matching_service.mentee.domain;

import com.haribo.matching_service.global.entity.BaseTimeEntity;
import com.haribo.matching_service.global.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Reservation")
public class Reservation extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Integer reservationId;

    @Column(name = "mento_id", nullable = false)
    private String mentoId;

    @Column(name = "mentee_id", nullable = false)
    private String menteeId;

    private ReservationStatus status;

    private String request;
}
