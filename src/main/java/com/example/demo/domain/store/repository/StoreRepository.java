package com.example.demo.domain.store.repository;

import com.example.demo.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByBusinessNumber(String businessNumber);

    Optional<Store> findByBusinessNumber(String businessNumber);

    Optional<Store> findByIdAndOwnerId(Long userId, Long storeId);



}
