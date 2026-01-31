package com.example.demo.domain.reservationItem.domain;

import com.example.demo.domain.menu.domain.Menu;
import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Menu_id")
    private Menu menu;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int count;

    @Enumerated(EnumType.STRING)
    private ReservationItemStatus status;

    @Builder
    public ReservationItem(Reservation reservation, Menu menu, int price, int count, ReservationItemStatus status){
        this.reservation = reservation;
        this.menu = menu;
        this.price = price;
        this.count = count;
        this.status = status;
    }
}
