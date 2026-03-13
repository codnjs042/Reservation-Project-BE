package com.example.demo.domain.store.domain;

import com.example.demo.domain.user.domain.User;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=50)
    private String name;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Column(nullable = false, length=50)
    private String address;

    @Column(nullable = false, length=11)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, length=10)
    private String businessNumber;

    @Column(nullable = false)
    private int slotInterval;

    @Column(nullable = false)
    private int favorites;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Builder
    public Store(String name, StoreCategory category, String address, String phone, User owner, String ownerName, String businessNumber, int slotInterval, int favorites, StoreStatus status){
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.owner = owner;
        this.ownerName = ownerName;
        this.businessNumber = businessNumber;
        this.slotInterval = slotInterval;
        this.favorites = favorites;
        this.status = status;
    }

    public void updateBasicInfo(String name, StoreCategory category, String phone){
        this.name = name;
        this.category = category;
        this.phone = phone;
    }

    public void updateBusinessInfo(String address, String ownerName, String businessNumber){
        this.address = address;
        this.ownerName = ownerName;
        this.businessNumber = businessNumber;
    }

    public void updateStatus(StoreStatus status){
        this.status = status;
    }
}
