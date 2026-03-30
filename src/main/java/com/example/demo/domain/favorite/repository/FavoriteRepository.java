package com.example.demo.domain.favorite.repository;

import com.example.demo.domain.favorite.domain.Favorite;
import com.example.demo.domain.favorite.domain.FavoriteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    Optional<Favorite> findRelation(
            @Param("userId") Long userId,
            @Param("storeId") Long storeId);

    @Query("""
            select f from Favorite f
            join fetch f.store
            join fetch f.user
            where f.user.id = :userId
            and f.status = :status
            """)
    List<Favorite> getFavorites(
            @Param("userId") Long userId,
            @Param("status") FavoriteStatus status);

    @Modifying
    @Query("""
            update Favorite f
            set f.status = :status
            where f.user.id = :userId""")
    void updateStatusByUser(
            @Param("userId") Long userId,
            @Param("status") FavoriteStatus status);

    @Modifying
    @Query("""
            update Favorite f
            set f.status = :status
            where f.store.id in :storeIds""")
    void updateStatusByStore(
            @Param("storeIds") List<Long> storeIds,
            @Param("status") FavoriteStatus status);

    boolean existsByUser_IdAndStore_IdAndStatus(
            Long userId,
            Long storeId,
            FavoriteStatus status);
}
