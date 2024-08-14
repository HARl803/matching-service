package com.haribo.matching_service.mentee.domain;

import com.haribo.matching_service.global.converter.ReservationStatusConverter;
import com.haribo.matching_service.global.entity.BaseTimeEntity;
import com.haribo.matching_service.global.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "mento_id", nullable = false)
    private String mentoId;

    @Column(name = "mentee_id", nullable = false)
    private String menteeId;

    @Convert(converter = ReservationStatusConverter.class)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    private String request;
}
