package com.example.demo.domain.storeTable.service;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import com.example.demo.domain.storeTable.dto.StoreTableRegisterRequest;
import com.example.demo.domain.storeTable.dto.StoreTableUpdateRequest;
import com.example.demo.domain.storeTable.repository.StoreTableRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreTableService {
    private final StoreTableRepository storeTableRepository;

    @Transactional
    public void create(Store store, StoreTableRegisterRequest dto){
        boolean isExists= storeTableRepository.existsByStore_IdAndTableNameAndStatus(store.getId(), dto.tableName(), StoreTableStatus.ACTIVE);

        if(isExists)
            throw new BusinessException(ErrorCode.TABLE_ALREADY_EXIST);

        //변경 전 테이블에 예약이 잡힌 경우 테이블 변경 불가(코드 작성 필요)

        for(int i=1; i<=dto.count(); i++){
            StoreTable storeTable = StoreTable.builder()
                    .store(store)
                    .tableName(dto.tableName())
                    .maxCapacity(dto.maxCapacity())
                    .minCapacity(dto.minCapacity())
                    .status(StoreTableStatus.ACTIVE)
                    .build();

            storeTableRepository.save(storeTable);
        }
    }

    @Transactional
    public void create(Store store, StoreTableUpdateRequest dto) {
        StoreTable storeTable = StoreTable.builder()
                .store(store)
                .tableName(dto.tableName())
                .maxCapacity(dto.maxCapacity())
                .minCapacity(dto.minCapacity())
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

    public void validateGroup(Long storeId, int headCount){
        boolean isFit = storeTableRepository.existsByStore_IdAndMaxCapacityGreaterThanEqualAndStatus(storeId, headCount, StoreTableStatus.ACTIVE);
        if(!isFit)
            throw new BusinessException(ErrorCode.RESERVATION_GROUP_LIMIT);
    }

    public StoreTable matchTable(Long storeId, LocalDateTime targetDateTime, int headCount){
        return storeTableRepository.findFreeTable(storeId, targetDateTime, ReservationStatus.CONFIRMED, headCount, StoreTableStatus.ACTIVE)
                .stream()
                .min(Comparator.comparingInt(StoreTable::getMaxCapacity))
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_FULL_TIME));
    }
}
