package com.example.demo.domain.storeTable.domain;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "storeTables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private String tableName;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private int minCapacity;

    @Column(nullable = false)
    private int count;

    @Enumerated(EnumType.STRING)
    private StoreTableStatus status;
}
