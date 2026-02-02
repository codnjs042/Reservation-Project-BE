package com.example.demo.domain.storeTable.service;

import com.example.demo.domain.menu.domain.Menu;
import com.example.demo.domain.menu.domain.MenuStatus;
import com.example.demo.domain.menu.dto.MenuRegisterRequest;
import com.example.demo.domain.menu.dto.MenuRegisterResponse;
import com.example.demo.domain.menu.repository.MenuRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.dto.StoreRegisterRequest;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import com.example.demo.domain.storeTable.dto.StoreTableRegisterRequest;
import com.example.demo.domain.storeTable.repository.StoreTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreTableService {
    private final StoreTableRepository storeTableRepository;
    private final StoreRepository storeRepository;

    public void register(Long userId, Long storeId, StoreTableRegisterRequest dto){
        Store store = storeRepository.findByIdAndOwnerId(storeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        Optional<StoreTable> existing = storeTableRepository.findByStoreIdAndTableName(storeId, dto.tableName());

        if(existing.isPresent())
            throw new IllegalArgumentException("이미 존재하는 테이블 타입입니다.");

        StoreTable storeTable = StoreTable.builder()
                .store(store)
                .tableName(dto.tableName())
                .maxCapacity(dto.maxCapacity())
                .minCapacity(dto.minCapacity())
                .count(dto.count())
                .status(StoreTableStatus.ACTIVE)
                .build();

        storeTableRepository.save(storeTable);
    }
}
