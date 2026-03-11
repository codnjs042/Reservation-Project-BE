package com.example.demo.domain.schedule.service;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import com.example.demo.domain.schedule.domain.ScheduleType;
import com.example.demo.domain.schedule.dto.ScheduleRegisterRequest;
import com.example.demo.domain.schedule.repository.ScheduleRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void register(Store store, ScheduleRegisterRequest dto){
        Schedule schedule = Schedule.builder()
                .store(store)
                .dayOfWeek(dto.dayOfWeek())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .type(dto.type())
                .status(ScheduleStatus.ACTIVE)
                .build();

        scheduleRepository.save(schedule);
    }

    // 가게의 특정 요일 운영 시간대 목록 찾기
    public List<Schedule> findDaySchedule(Long storeId, DayOfWeek dayOfWeek){
        return scheduleRepository.findByStoreIdAndDayOfWeekAndTypeOrderByStartTimeAsc(storeId, dayOfWeek, ScheduleType.OPEN);
    }
}
