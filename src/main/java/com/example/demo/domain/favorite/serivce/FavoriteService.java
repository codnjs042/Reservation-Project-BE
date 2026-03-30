package com.example.demo.domain.favorite.serivce;

import com.example.demo.domain.favorite.domain.Favorite;
import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.favorite.dto.FavoriteResponse;
import com.example.demo.domain.favorite.repository.FavoriteRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void create(User user, Store store) {
        Favorite favorite = Favorite.builder()
                .user(user)
                .store(store)
                .status(FavoriteStatus.ACTIVE)
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public FavoriteStatus toggle(User user, Store store){
        return favoriteRepository.findRelation(user.getId(), store.getId())
                .map(Favorite::toggle)
                .orElseGet(() -> {
                    create(user, store);
                    return FavoriteStatus.ACTIVE;
                });
    }

    public List<FavoriteResponse> getList(Long userId){
        List<Favorite> favorites = favoriteRepository.getFavorites(userId, FavoriteStatus.ACTIVE);
        return favorites.stream().map(FavoriteResponse::from).toList();
    }

    @Transactional
    public void updateStatusByUser(Long userId) {
        favoriteRepository.updateStatusByUser(userId, FavoriteStatus.DELETED);
    }

    @Transactional
    public void bulkUpdateStatusByStore(List<Long> storeIds) {
        favoriteRepository.updateStatusByStore(storeIds, FavoriteStatus.DELETED);
    }

    public boolean existsByUser_IdAndStore_IdAndStatus(Long userId, Long storeId){
        return favoriteRepository.existsByUser_IdAndStore_IdAndStatus(userId, storeId, FavoriteStatus.ACTIVE);
    }
}
