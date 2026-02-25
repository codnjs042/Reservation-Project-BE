package com.example.demo.domain.reservation.repository;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r " +
            "where (:keyword is null or :keyword='' or " +
                "(:type='id' and cast(r.id as string) like %:keyword%) or" +
                "(:type='name' and r.name like %:keyword%))" +
            "and r.store.id = :storeId " +
            "and cast(r.targetDateTime as date) >= :startDate " +
            "and cast(r.targetDateTime as date) <= :endDate " +
            "and (:status is null or r.status in :status)")
    List<Reservation> getReservation(
                @Param("type") String type,
                @Param("keyword") String keyword,
                @Param("storeId") Long storeId,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate,
                @Param("status") List<ReservationStatus> status);

    List<Reservation> findByStoreIdAndTargetDateTimeAndStatus(Long storeId, LocalDateTime targetDateTime, ReservationStatus status);

    @Query("select count(r)>0 from Reservation r " +
            "where r.targetDateTime > :targetDateTime " +
            "and r.storeTable.id = :tableId " +
            "and r.status = :status")
    boolean hasFutureReservation(
            @Param("targetDateTime") LocalDateTime targetDateTime,
            @Param("tableId") Long tableId,
            @Param("status") ReservationStatus status);

    @Query("""
            select count(r) from Reservation r
            where r.targetDateTime > :targetDateTime
            and r.storeTable.id = :tableId
            and r.status = :status
            group by r.targetDateTime
            order by count(r) desc
            """)
    List<Long> countFutureReservation(
            @Param("targetDateTime") LocalDateTime targetDateTime,
            @Param("tableId") Long tableId,
            @Param("status") ReservationStatus status);
}
