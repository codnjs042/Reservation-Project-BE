package com.example.demo.domain.storeTable.repository;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
    List<StoreTable> findByStoreId(Long storeId);

    //동일 테이블 존재 여부
    boolean existsByStore_IdAndTableNameAndStatus(
            Long storeId,
            String tableName,
            StoreTableStatus status);

    //예약 가능 테이블 현황
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value="3000")})
    @Query("""
            select t from StoreTable t
                where t.store.id = :storeId
                and :headCount between t.minCapacity and t.maxCapacity
                and t.status = :storeTableStatus
                and not exists (
                    select 1 from Reservation r
                    where r.targetDateTime = :targetDateTime
                    and r.storeTable.id = t.id
                    and r.status = :reservationStatus)
            """)
     List<StoreTable> findFreeTable(
            @Param("storeId") Long storeId,
            @Param("targetDateTime") LocalDateTime targetDateTime,
            @Param("reservationStatus") ReservationStatus reservationStatus,
            @Param("headCount") int headCount,
            @Param("storeTableStatus") StoreTableStatus storeTableStatus);

    //수용 가능한 테이블 존재 여부
    boolean existsByStore_IdAndMaxCapacityGreaterThanEqualAndStatus(
            Long storeId,
            int headCount,
            StoreTableStatus status);
}
