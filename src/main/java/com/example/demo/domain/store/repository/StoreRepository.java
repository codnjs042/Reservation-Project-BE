package com.example.demo.domain.store.repository;

import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    //가게 중복 등록 방지용
    boolean existsByBusinessNumberAndStatusNot(
            String businessNumber,
            StoreStatus status);

    @Query("""
            select s from Store s
            where (:keyword is null or :keyword='' or
                (s.name like %:keyword%) or
                (s.category in :keywordCategories) or
                (s.address like %:keyword%))
            and (:category is null or s.category = :category)
            and s.status in :status
            """)
    List<Store> getList(
            @Param("keyword") String keyword,
            @Param("keywordCategories") List<StoreCategory> keywordCategories,
            @Param("category") StoreCategory category,
            @Param("status") List<StoreStatus> status);

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

    @Modifying
    @Query("""
            update Store s
            set s.status = :status
            where s.id in :storeIds
            """)
    void bulkUpdateStatus(
            @Param("storeIds") List<Long> storeIds,
            @Param("status") StoreStatus status);

    @Query("""
            select s from Store s
            where s.owner.id = :userId
            and s.status != :status
            """)
    List<Store> getMyStores(
            @Param("userId") Long userId,
            @Param("status") StoreStatus status);

    @Query("""
            select f.store from Favorite f
            where f.store.status in :storeStatus
            and f.status = :favoriteStatus
            group by f.store
            order by count(f) desc, f.store.id desc
            """)
    List<Store> getFamous(
            @Param("storeStatus") List<StoreStatus> storeStatus,
            @Param("favoriteStatus") FavoriteStatus favoriteStatus,
            Pageable pageable);

    List<Store> findTop12ByStatusOrderByCreatedAtDesc(StoreStatus status);

    @Modifying
    @Query("""
            update Store s
            set s.status = :status
            where s.owner.id = :userId
            """)
    void updateAllStores(
            @Param("userId") Long userId,
            @Param("status") StoreStatus status);

    @Query("""
            select s from Store s
            where (:keyword is null or :keyword='' or
                (cast(s.id as string) like %:keyword%) or
                (s.name like %:keyword%) or
                (s.businessNumber like %:keyword%))
            and (:category is null or s.category = :category)
            and (:status is null or s.status = :status)
            """)
    List<Store> getStoresForAdmin(
            @Param("keyword") String keyword,
            @Param("category") StoreCategory category,
            @Param("status") StoreStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value="3000")})
    @Query("""
            select s from Store s
            where s.id = :id
            """)
    Optional<Store> findByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value="3000")})
    @Query("""
            select s from Store s
            where s.id in :ids
            """)
    List<Store> findAllByIdWithLock(@Param("ids") List<Long> ids);
}
