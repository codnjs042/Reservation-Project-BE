package com.example.demo.domain.reservation.domain;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name="reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    private Store store;

    @Column(nullable = false)
    private LocalDateTime targetDateTime;

    @Column(nullable = false)
    private int headCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableId")
    private StoreTable storeTable;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Builder
    public Reservation(User user, Store store, LocalDateTime targetDateTime, int headCount, StoreTable storeTable, ReservationStatus status){
        this.user = user;
        this.store = store;
        this.targetDateTime=targetDateTime;
        this.headCount = headCount;
        this.storeTable = storeTable;
        this.status = status;
    }
}
