package com.example.demo.domain.reservation.repository;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStoreIdAndTargetDateTimeAndStatus(Long storeId, LocalDateTime targetDateTime, ReservationStatus status);
}
