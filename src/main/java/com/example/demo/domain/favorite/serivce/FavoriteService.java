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
import java.util.Optional;

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
        Optional<Favorite> existing = favoriteRepository.findRelation(user.getId(), store.getId());

        if(existing.isPresent()){
            Favorite favorite = existing.get();
            return favorite.toggle();
        }
        else{
            create(user, store);
            return FavoriteStatus.ACTIVE;
        }

    }

    public List<FavoriteResponse> getList(Long userId){
        List<Favorite> favorites = favoriteRepository.getFavorites(userId, FavoriteStatus.ACTIVE);
        return favorites.stream().map(FavoriteResponse::from).toList();
    }

    public long countFans(Long storeId){
        return favoriteRepository.countFans(storeId, FavoriteStatus.ACTIVE);
    }

    @Transactional
    public void updateStatusByUser(Long userId) {
        favoriteRepository.updateStatusByUser(userId, FavoriteStatus.DELETED);
    }

    @Transactional
    public void updateStatusByStore(Long storeId) {
        favoriteRepository.updateStatusByStore(storeId, FavoriteStatus.DELETED);
    }
}
