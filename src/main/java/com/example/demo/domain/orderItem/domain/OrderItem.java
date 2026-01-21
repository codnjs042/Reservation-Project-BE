package com.example.demo.domain.orderItem.domain;

import com.example.demo.domain.menu.domain.Menu;
import com.example.demo.domain.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Menu_id")
    private Menu menu;

    @Column
    private int price;

    @Column
    private int count;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;
}
