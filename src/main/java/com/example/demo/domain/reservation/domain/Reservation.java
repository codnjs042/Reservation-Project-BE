package com.example.demo.domain.reservation.domain;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

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
    private int headCount;

    @Column(nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Builder
    public Reservation(User user, Store store, int headCount, LocalTime time, ReservationStatus status){
        this.user = user;
        this.store = store;
        this.headCount = headCount;
        this.time = time;
        this.status = status;
    }
}
