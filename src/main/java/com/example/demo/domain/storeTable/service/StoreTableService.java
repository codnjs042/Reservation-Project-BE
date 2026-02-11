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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreTableService {
    private final StoreTableRepository storeTableRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    public void register(Long userId, Long storeId, List<StoreTableRegisterRequest> dtos){
        Store store = storeRepository.findByIdAndOwnerId(storeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        for(StoreTableRegisterRequest dto : dtos){
            Optional<StoreTable> existing = storeTableRepository.findByStoreIdAndTableName(storeId, dto.tableName());

            if(existing.isPresent())
                throw new IllegalArgumentException("이미 등록된 테이블 형식입니다. " + dto.tableName());

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

    public List<StoreTableResponse> findById(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        List<StoreTable> storeTables = storeTableRepository.findByStoreId(store.getId());

        return storeTables.stream()
                .map(StoreTableResponse::from)
                .toList();
    }

    public void modify(Long userId, Long storeId, List<StoreTableUpdateRequest> dtos){
        Store store = storeRepository.findByIdAndOwnerId(storeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        Set<String> inputNames = dtos.stream()
                .map(StoreTableUpdateRequest::tableName)
                .collect(Collectors.toSet());

        if(inputNames.size() != dtos.size())
            throw new IllegalArgumentException("테이블 형식은 중복 불가입니다.");

        for(StoreTableUpdateRequest dto : dtos){
            if(dto.id()==null) {
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
            else{
                StoreTable storeTable = storeTableRepository.findById(dto.id())
                        .orElseThrow(() -> new IllegalArgumentException("해당 테이블을 찾을 수 없습니다."));

                if(storeTable.getStatus()==StoreTableStatus.ACTIVE && dto.status()==StoreTableStatus.DELETED)
                    if(reservationRepository.hasFutureReservation(LocalDateTime.now(), dto.id(), ReservationStatus.CONFIRMED))
                        throw new IllegalArgumentException("해당 테이블은 예약 중이므로 삭제할 수 없습니다. " + dto.tableName());

                List<Long> counts = reservationRepository.countFutureReservation(LocalDateTime.now(), dto.id(), ReservationStatus.CONFIRMED);
                if(!counts.isEmpty() && counts.getFirst()>dto.count())
                    throw new IllegalArgumentException("해당 테이블의 동시간대 최대 예약 건수보다 적게 수정할 수 없습니다.");

                storeTable.modify(dto.tableName(), dto.minCapacity(), dto.maxCapacity(), dto.count(), dto.status());
            }
        }
    }
}
