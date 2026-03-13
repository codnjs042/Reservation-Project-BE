package com.example.demo.domain.schedule.service;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import com.example.demo.domain.schedule.domain.ScheduleType;
import com.example.demo.domain.schedule.dto.ScheduleUpsertRequest;
import com.example.demo.domain.schedule.repository.ScheduleRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void upsert(Store store, DayOfWeek dayOfWeek, List<ScheduleUpsertRequest> dtos){
        List<Schedule> schedules = scheduleRepository.findDaySchedules(store.getId(), dayOfWeek, ScheduleStatus.ACTIVE);

        //변경 전 운영 시간대에 예약이 잡힌 경우 시간대 변경 불가(코드 작성 필요)

        schedules.forEach(s -> s.updateStatus(ScheduleStatus.DELETED));

        List<ScheduleUpsertRequest> sorted = dtos.stream()
                .sorted(Comparator.comparing(ScheduleUpsertRequest::startTime))
                .toList();

        boolean hasConflict = IntStream.range(0, sorted.size()-1)
                .anyMatch(i -> !sorted.get(i).endTime().isBefore(sorted.get(i+1).startTime()));

        if(hasConflict)
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_TIME);

        List<Schedule> newSchedules = dtos.stream()
                .map(
                dto -> Schedule.builder()
                        .store(store)
                        .dayOfWeek(dayOfWeek)
                        .startTime(dto.startTime())
                        .endTime(dto.endTime())
                        .intervalMinute(dto.intervalMinute())
                        .status(ScheduleStatus.ACTIVE)
                        .build())
                .toList();

        scheduleRepository.saveAll(newSchedules);
    }

    // 가게의 특정 요일 운영 시간대 목록 찾기
    public List<Schedule> findDaySchedule(Long storeId, DayOfWeek dayOfWeek){
        return scheduleRepository.findByStoreIdAndDayOfWeekAndTypeOrderByStartTimeAsc(storeId, dayOfWeek, ScheduleType.OPEN);
    }
}
