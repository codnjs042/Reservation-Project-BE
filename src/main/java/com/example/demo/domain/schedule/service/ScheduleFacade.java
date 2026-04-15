package com.example.demo.domain.schedule.service;

import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.domain.schedule.dto.ScheduleUpsertRequest;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleFacade {
    private final ScheduleService scheduleService;
    private final StoreService storeService;
    private final ReservationService reservationService;

    @Transactional
    public void upsert(Long userId, Long storeId, DayOfWeek dayOfWeek, List<ScheduleUpsertRequest> dtos){
        Store store = storeService.findByIdWithLock(storeId);

        storeService.validateOwner(store, userId);

        reservationService.validateTime(store.getId(), dayOfWeek);
        scheduleService.upsert(store, dayOfWeek, dtos);
        if(dtos==null || dtos.isEmpty()){
            boolean hasSchedule = scheduleService.existsByStoreIdAndStatus(store.getId());
            if(!hasSchedule)
                store.updateStatus(StoreStatus.READY);
        }
    }
}
