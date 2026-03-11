package com.example.demo.domain.storeTable.service;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.repository.ReservationRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import com.example.demo.domain.storeTable.dto.StoreTableRegisterRequest;
import com.example.demo.domain.storeTable.dto.StoreTableResponse;
import com.example.demo.domain.storeTable.dto.StoreTableUpdateRequest;
import com.example.demo.domain.storeTable.repository.StoreTableRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreTableService {
    private final StoreTableRepository storeTableRepository;

    @Transactional
    public void register(Store store, StoreTableRegisterRequest dto){
        Optional<StoreTable> existing = storeTableRepository.findByStoreIdAndTableName(store.getId(), dto.tableName());

        if(existing.isPresent())
            throw new BusinessException(ErrorCode.TABLE_ALREADY_EXIST);

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

    @Transactional
    public void register(Store store, StoreTableUpdateRequest dto) {
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

    public StoreTable findById(Long storeTableId){
        return storeTableRepository.findById(storeTableId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TABLE_NOT_FOUND));
    }

    public List<StoreTable> findByIds(Long storeId){
        return storeTableRepository.findByStoreId(storeId);
    }

    //가게 내 예약인원을 허용하는 테이블 찾기
    public List<StoreTable> findBySeat(Long storeId, int headCount){
        return storeTableRepository.findBySeat(storeId, headCount, headCount);
    }

    //락버전
    public List<StoreTable> findBySeatWithLock(Long storeId, int headCount){
        return storeTableRepository.findBySeatWithLock(storeId, headCount, headCount);
    }
}
