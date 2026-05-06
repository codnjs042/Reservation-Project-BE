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
    private String zipcode;

    @Column(nullable = false, length = 5)
    private String sigunguCode;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false, length=11)
    private String phone;

    @Column(nullable = false)
    private boolean isPartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(nullable = false, length=50)
    private String ownerName;

    @Column(nullable = false, length=10)
    private String businessNumber;

    @Column(nullable = false)
    private int favorites = 0;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Builder
    public Store(String name, StoreCategory category, String address, String detailAddress, String zipcode, String sigunguCode, Double latitude, Double longitude, String phone, boolean isPartner, User owner, String ownerName, String businessNumber, StoreStatus status){
        this.name = name;
        this.category = category;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;
        this.sigunguCode = sigunguCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.isPartner = isPartner;
        this.owner = owner;
        this.ownerName = ownerName;
        this.businessNumber = businessNumber;
        this.status = status;
    }

    public void updateBasicInfo(String name, StoreCategory category, String address, String phone){
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
    }

    public void updateBusinessInfo(String ownerName, String businessNumber){
        this.ownerName = ownerName;
        this.businessNumber = businessNumber;
    }

    public void updateStatus(StoreStatus status){
        this.status = status;
    }
}
