package com.example.demo.domain.reservation.repository;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStoreIdAndTargetDateTimeAndStatus(Long storeId, LocalDateTime targetDateTime, ReservationStatus status);

    @Query("select count(r)>0 from Reservation r " +
            "where r.targetDateTime > :targetDateTime " +
            "and r.storeTable.id = :tableId " +
            "and r.status = :status")
    boolean hasFutureReservation(
            @Param("targetDateTime") LocalDateTime targetDateTime,
            @Param("tableId") Long tableId,
            @Param("status") ReservationStatus status);

    @Query("select count(r) from Reservation r " +
            "where r.targetDateTime > :targetDateTime " +
            "and r.storeTable.id = :tableId " +
            "and r.status = :status" +
            "group by r.targetDateTime" +
            "order by count(r) desc")
    List<Long> countFutureReservation(
            @Param("targetDateTime") LocalDateTime targetDateTime,
            @Param("tableId") Long tableId,
            @Param("status") ReservationStatus status);
}
