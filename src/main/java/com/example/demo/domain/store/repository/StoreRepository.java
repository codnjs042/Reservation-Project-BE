package com.example.demo.domain.store.repository;

import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    //가게 중복 등록 방지용
    boolean existsByBusinessNumberAndStatus(
            String businessNumber,
            StoreStatus status);

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

    @Modifying
    @Query("""
            update Store s
            set s.favorites = s.favorites + :delta
            where s.id = :id
            """)
    void updateFavorites(
            @Param("id") Long id,
            @Param("delta") int delta);

    @Modifying
    @Query("""
            update Store s
            set s.favorites = s.favorites + :delta
            where s.id in (
                select f.store.id from Favorite f
                where f.user.id = :userId
                and f.status = :status)
            """)
    void updateAllFavorites(
            @Param("delta") int delta,
            @Param("userId") Long userId,
            @Param("status") FavoriteStatus status);
}
