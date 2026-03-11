package com.example.demo.domain.schedule.service;

import com.example.demo.domain.schedule.dto.ScheduleRegisterRequest;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleFacade {
    private final ScheduleService scheduleService;
    private final StoreService storeService;

    @Transactional
    public void register(Long userId, Long storeId, ScheduleRegisterRequest dto){
        Store store = storeService.findById(storeId);

        if(!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        scheduleService.register(store, dto);
    }
}
