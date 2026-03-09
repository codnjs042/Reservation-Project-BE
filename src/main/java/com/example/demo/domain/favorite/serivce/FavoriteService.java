package com.example.demo.domain.favorite.serivce;

import com.example.demo.domain.favorite.domain.Favorite;
import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.favorite.dto.FavoriteResponse;
import com.example.demo.domain.favorite.repository.FavoriteRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
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
    private final StoreRepository storeRepository;

    @Transactional
    public void toggleFavorite(User user, Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        Optional<Favorite> existing = favoriteRepository.getFavorite(user.getId(), storeId);

        if (existing.isEmpty()) {
            Favorite favorite = Favorite.builder()
                    .user(user)
                    .store(store)
                    .status(FavoriteStatus.ACTIVE)
                    .build();
            favoriteRepository.save(favorite);
        }
        else{
            Favorite favorite = existing.get();

            if(favorite.getStatus() == FavoriteStatus.ACTIVE)
                favorite.updateStatus(FavoriteStatus.DELETED);
            else
                favorite.updateStatus(FavoriteStatus.ACTIVE);
        }
    }

    public List<FavoriteResponse> getList(Long userId){
        List<Favorite> favorite = favoriteRepository.getFavoriteList(userId);
        return favorite.stream().map(FavoriteResponse::from).toList();
    }
}
