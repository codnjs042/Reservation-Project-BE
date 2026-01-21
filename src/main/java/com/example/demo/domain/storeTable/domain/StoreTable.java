package com.example.demo.domain.storeTable.domain;

import com.example.demo.domain.store.domain.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "storeTables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column
    private String tableName;

    @Column
    private int maxCapacity;

    @Column
    private int minCapacity;

    @Column
    private int count;

    @Column
    private StoreTableStatus status;
}
