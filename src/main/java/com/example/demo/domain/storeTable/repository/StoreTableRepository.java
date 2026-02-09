package com.example.demo.domain.storeTable.repository;

import com.example.demo.domain.storeTable.domain.StoreTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
    List<StoreTable> findByStoreId(Long storeId);

    Optional<StoreTable> findByStoreIdAndTableName(Long storeId, String tableName);

    @Query("select * from storeTables t " +
            "where t.store_id = :storeId " +
            "and t.minCapacity <= :minCapacity " +
            "and t.maxCapacity >= :maxCapacity")
    List<StoreTable> findBySeat(@Param("storeId") Long storeId,
                                @Param("minCapacity") int minCapacity,
                                @Param("maxCapacity") int maxCapacity);
}
