package com.example.demo.domain.reservation.repository;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    //가게 예약 목록 조회
    @Query("""
            select r from Reservation r
            where (:keyword is null or :keyword='' or
                (:type='id' and cast(r.id as string) like %:keyword%) or
                (:type='name' and r.name like %:keyword%))
            and r.store.id = :storeId
            and cast(r.targetDateTime as date) between :startDate and :endDate
            and (:status is null or r.status in :status)
            """)
    List<Reservation> getStoreReservationList(
                @Param("type") String type,
                @Param("keyword") String keyword,
                @Param("storeId") Long storeId,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate,
                @Param("status") List<ReservationStatus> status);

    //유저 예약 목록 조회
    @Query("""
            select r from Reservation r
            where (:keyword is null or :keyword='' or
                (:type='id' and cast(r.id as string) like %:keyword%) or
                (:type='storeName' and r.store.name like %:keyword%))
            and r.user.id = :userId
            and cast(r.targetDateTime as date) between :startDate and :endDate
            and (:status is null or r.status in :status)
            """)
    List<Reservation> getMyReservationList(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") List<ReservationStatus> status);

    //예약 마감 시간대 조회
    @Query("""
            select r.targetDateTime from Reservation r
            where r.store.id = :storeId
            and r.targetDateTime between :start and :end
            and r.status = :reservationStatus
            group by r.targetDateTime
            having count(r.storeTable.id) >= (
                select count(t.id) from StoreTable t
                where t.store.id = :storeId
                and :headCount between t.minCapacity and t.maxCapacity
                and t.status = :storeTableStatus)
            """)
    List<LocalDateTime> findFullTimes(
            @Param("storeId") Long storeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("reservationStatus") ReservationStatus reservationStatus,
            @Param("headCount") int headCount,
            @Param("storeTableStatus") StoreTableStatus storeTableStatus);

    boolean existsByTargetDateTimeGreaterThanAndStoreTable_IdAndStatus(
            LocalDateTime targetDateTime,
            Long tableId,
            ReservationStatus status);

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
