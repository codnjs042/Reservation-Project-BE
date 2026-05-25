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

    @Column(nullable = false, length=100)
    private String address;

    @Column(length=50)
    private String detailAddress;

    @Column(nullable = false, length=100)
    private String zipCode;

    @Column(nullable = false, length = 5)
    private String sigunguCode;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false, length=11)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(length=50)
    private String ownerName;

    @Column(nullable = false, length=10)
    private String businessNumber;

    @Column(nullable = false)
    private int favorites = 0;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Builder
    public Store(String name, StoreCategory category, String address, String detailAddress, String zipCode, String sigunguCode, Double latitude, Double longitude, String phone, User owner, String ownerName, String businessNumber, StoreStatus status){
        this.name = name;
        this.category = category;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
        this.sigunguCode = sigunguCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.owner = owner;
        this.ownerName = ownerName;
        this.businessNumber = businessNumber;
        this.status = status;
    }

    public void updateBasicInfo(String name, StoreCategory category, String address, String detailAddress, String zipCode, String sigunguCode, Double latitude, Double longitude, String phone){
        this.name = name;
        this.category = category;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
        this.sigunguCode = sigunguCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    public void updateStatus(StoreStatus status){
        this.status = status;
    }
}
