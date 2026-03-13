package com.example.demo.domain.storeTable.service;

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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreTableService {
    private final StoreTableRepository storeTableRepository;

    @Transactional
    public void create(Store store, StoreTableRegisterRequest dto){
        boolean isExists= storeTableRepository.hasTable(store.getId(), dto.tableName(), StoreTableStatus.ACTIVE);

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

    //가게 내 예약인원을 허용하는 테이블 찾기
    public List<StoreTable> findBySeat(Long storeId, int headCount){
        return storeTableRepository.findBySeat(storeId, headCount, headCount);
    }

    //락버전
    public List<StoreTable> findBySeatWithLock(Long storeId, int headCount){
        return storeTableRepository.findBySeatWithLock(storeId, headCount, headCount);
    }
}
