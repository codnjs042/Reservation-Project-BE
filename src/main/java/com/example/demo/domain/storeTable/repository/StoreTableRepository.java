package com.example.demo.domain.storeTable.repository;

import com.example.demo.domain.menu.domain.Menu;
import com.example.demo.domain.storeTable.domain.StoreTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
    Optional<StoreTable> findByStoreIdAndTableName(Long storeId, String tableName);
}
