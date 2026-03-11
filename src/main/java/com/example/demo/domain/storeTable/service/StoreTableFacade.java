package com.example.demo.domain.storeTable.service;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import com.example.demo.domain.storeTable.dto.StoreTableRegisterRequest;
import com.example.demo.domain.storeTable.dto.StoreTableResponse;
import com.example.demo.domain.storeTable.dto.StoreTableUpdateRequest;
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
public class StoreTableFacade {
    private final StoreTableService storeTableService;
    private final StoreService storeService;
    private final ReservationService reservationService;

    @Transactional
    public void register(Long userId, Long storeId, List<StoreTableRegisterRequest> dtos){
        Store store = storeService.findById(storeId);

        if(!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        for(StoreTableRegisterRequest dto : dtos){
            storeTableService.register(store, dto);
        }
    }

    public List<StoreTableResponse> findByIds(Long storeId){
        Store store = storeService.findById(storeId);

        List<StoreTable> storeTables = storeTableService.findByIds(storeId);

        return storeTables.stream()
                .map(StoreTableResponse::from)
                .toList();
    }

    @Transactional
    public void modify(Long userId, Long storeId, List<StoreTableUpdateRequest> dtos){
        Store store = storeService.findById(storeId);

        if(!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        Set<String> inputNames = dtos.stream()
                .map(StoreTableUpdateRequest::tableName)
                .collect(Collectors.toSet());

        if(inputNames.size() != dtos.size())
            throw new BusinessException(ErrorCode.DUPLICATE_TABLE_INPUT);

        for(StoreTableUpdateRequest dto : dtos){
            if(dto.id()==null) {
                storeTableService.register(store, dto);
            }
            else{
                StoreTable storeTable = storeTableService.findById(dto.id());

                if(storeTable.getStatus()==StoreTableStatus.ACTIVE && dto.status()==StoreTableStatus.DELETED)
                    if(reservationService.hasFutureReservation(dto.id()))
                        throw new BusinessException(ErrorCode.TABLE_DELETE_RESERVATION_EXIST);

                List<Long> counts = reservationService.countFutureReservation(dto.id());
                if(!counts.isEmpty() && counts.getFirst()>dto.count())
                    throw new BusinessException(ErrorCode.TABLE_UPDATE_RESERVATION_EXIST);

                storeTable.modify(dto.tableName(), dto.minCapacity(), dto.maxCapacity(), dto.count(), dto.status());
            }
        }
    }
}
