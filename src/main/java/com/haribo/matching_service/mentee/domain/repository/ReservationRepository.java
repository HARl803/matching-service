package com.haribo.matching_service.mentee.domain.repository;

import com.haribo.matching_service.mentee.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
