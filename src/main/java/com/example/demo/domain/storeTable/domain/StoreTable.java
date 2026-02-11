package com.example.demo.domain.storeTable.domain;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private int minCapacity;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private int count;

    @Enumerated(EnumType.STRING)
    private StoreTableStatus status;

    @Builder
    public StoreTable(Store store, String tableName, int minCapacity, int maxCapacity, int count, StoreTableStatus status){
        this.store = store;
        this.tableName = tableName;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.count = count;
        this.status = status;
    }

    public void modify(String tableName, int minCapacity, int maxCapacity, int count, StoreTableStatus status){
        this.tableName = tableName;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.count = count;
        this.status = status;
    }
}
