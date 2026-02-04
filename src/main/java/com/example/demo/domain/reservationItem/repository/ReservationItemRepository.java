package com.example.demo.domain.reservationItem.repository;

import com.example.demo.domain.reservationItem.domain.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long> {
}
