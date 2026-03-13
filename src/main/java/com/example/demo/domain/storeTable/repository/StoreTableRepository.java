package com.example.demo.domain.storeTable.repository;

import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
    List<StoreTable> findByStoreId(Long storeId);

    @Query("""
            select count(t)>0 from StoreTable t
            where t.store.id = :storeId
            and t.tableName = :tableName
            and t.status =:status
            """)
    boolean hasTable(
            @Param("storeId") Long storeId,
            @Param("tableName") String tableName,
            @Param("status") StoreTableStatus status);

    @Query("select t from StoreTable t " +
            "where t.store.id = :storeId " +
            "and t.minCapacity <= :minCapacity " +
            "and t.maxCapacity >= :maxCapacity")
    List<StoreTable> findBySeat(
            @Param("storeId") Long storeId,
            @Param("minCapacity") int minCapacity,
            @Param("maxCapacity") int maxCapacity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value="3000")})
    @Query("select t from StoreTable t " +
            "where t.store.id = :storeId " +
            "and t.minCapacity <= :minCapacity " +
            "and t.maxCapacity >= :maxCapacity")
    List<StoreTable> findBySeatWithLock(
            @Param("storeId") Long storeId,
            @Param("minCapacity") int minCapacity,
            @Param("maxCapacity") int maxCapacity);
}
