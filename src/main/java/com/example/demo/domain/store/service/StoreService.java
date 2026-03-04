package com.example.demo.domain.store.service;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.dto.StoreRegisterRequest;
import com.example.demo.domain.store.dto.StoreRegisterResponse;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public StoreRegisterResponse register(User user, StoreRegisterRequest dto){
        Optional<Store> existing= storeRepository.findByBusinessNumber(dto.businessNumber());

        if(existing.isPresent())
            throw new BusinessException(ErrorCode.STORE_ALREADY_EXIST);

        Store store = Store.builder()
                .name(dto.name())
                .category(dto.category())
                .address(dto.address())
                .phone(dto.phone())
                .owner(user)
                .businessNumber(dto.businessNumber())
                .slotInterval(dto.slotInterval())
                .usageTime(dto.usageTime())
                .status(StoreStatus.PENDING)
                .build();

        storeRepository.save(store);

        return StoreRegisterResponse.from(store);
    }
}
