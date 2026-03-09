package com.example.demo.domain.favorite.domain;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    private Store store;

    @Enumerated(EnumType.STRING)
    private FavoriteStatus status;

    @Builder
    public Favorite(User user, Store store, FavoriteStatus status){
        this.user = user;
        this.store = store;
        this.status = status;
    }

    public FavoriteStatus updateStatus(FavoriteStatus status){
        this.status = status;
        return status;
    }
}
