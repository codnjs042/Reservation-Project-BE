package com.example.demo.domain.store.repository;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByBusinessNumber(String businessNumber);

    Optional<Store> findByBusinessNumber(String businessNumber);

    @Query("""
            select s from Store s
            where (:keyword is null or :keyword='' or
                (s.name like %:keyword%) or
                (s.category like %:keyword%) or
                (s.address like %:keyword%))
            and s.status = :status
            """)
    List<Store> getList(
            @Param("keyword") String keyword,
            @Param("status") StoreStatus status);
}
