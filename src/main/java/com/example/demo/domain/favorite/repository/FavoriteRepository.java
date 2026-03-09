package com.example.demo.domain.favorite.repository;

import com.example.demo.domain.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("""
            select f from Favorite f
            where f.user.id = :userId
            and f.store.id = :storeId
            """)
    Optional<Favorite> getFavorite(
            @Param("userId") Long userId,
            @Param("storeId") Long storeId);

    @Query("""
            select f from Favorite f
            where f.user.id = :userId
            """)
    List<Favorite> getFavoriteList(@Param("userId") Long userId);
}
