package com.example.demo.domain.storeTable.service;

import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.dto.StoreTableCreateRequest;
import com.example.demo.domain.storeTable.dto.StoreTableResponse;
import com.example.demo.domain.storeTable.dto.StoreTableBulkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreTableFacade {
    private final StoreTableService storeTableService;
    private final StoreService storeService;
    private final ReservationService reservationService;

    @Transactional
    public void register(Long userId, Long storeId, StoreTableCreateRequest dto){
        Store store = storeService.findById(storeId);

        storeService.validateOwner(store, userId);

        storeTableService.validateName(store.getId(), dto.tableName());
        storeTableService.create(store, dto.tableName(), dto.minCapacity(), dto.maxCapacity(), dto.count());
    }

    public List<StoreTableResponse> findAllTables(Long storeId){
        Store store = storeService.findById(storeId);

        List<StoreTable> storeTables = storeTableService.findAllTables(store.getId());

        return storeTables.stream()
                .map(StoreTableResponse::from)
                .toList();
    }

    @Transactional
    public void adjust(Long userId, Long storeId, StoreTableBulkUpdateRequest dto){
        Store store = storeService.findById(storeId);

        storeService.validateOwner(store, userId);

        storeTableService.findTableGroup(store.getId(), dto.oldTableName());

        if(!dto.oldTableName().equals(dto.newTableName()))
            storeTableService.validateName(store.getId(), dto.newTableName());
        reservationService.validateCapacity(store.getId(),dto.minCapacity(), dto.maxCapacity(), dto.oldTableName());
        storeTableService.validateCount(store.getId(), dto.oldTableName(), dto.count());

        storeTableService.adjust(store, dto.oldTableName(), dto);
    }
}
