package com.example.demo.domain.menu.domain;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false, unique = true, length=50)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int limit;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    @Builder
    public Menu(Store store, String name, int price, int limit, MenuStatus status){
        this.store = store;
        this.name = name;
        this.price = price;
        this.limit = limit;
        this.status = status;
    }
}
