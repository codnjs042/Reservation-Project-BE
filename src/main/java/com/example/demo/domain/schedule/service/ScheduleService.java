package com.example.demo.domain.schedule.service;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import com.example.demo.domain.schedule.dto.ScheduleRegisterRequest;
import com.example.demo.domain.schedule.repository.ScheduleRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final StoreRepository storeRepository;

    public void register(Long userId, Long storeId, ScheduleRegisterRequest dto){
        Store store = storeRepository.findByIdAndOwnerId(storeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        Schedule schedule = Schedule.builder()
                .store(store)
                .dayOfWeek(dto.dayOfWeek())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .lastOrderTime(dto.lastOrderTime())
                .type(dto.type())
                .status(ScheduleStatus.ACTIVE)
                .build();

        scheduleRepository.save(schedule);
    }
}
