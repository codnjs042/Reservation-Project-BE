package com.example.demo.domain.schedule.service;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import com.example.demo.domain.schedule.dto.ScheduleUpsertRequest;
import com.example.demo.domain.schedule.repository.ScheduleRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<Schedule> findDaySchedules(Long storeId, DayOfWeek dayOfWeek){
        return scheduleRepository.findDaySchedules(storeId, dayOfWeek, ScheduleStatus.ACTIVE);
    }

    public List<LocalTime> generateSlots(Long storeId, DayOfWeek dayOfWeek){
        List<Schedule> schedules = findDaySchedules(storeId, dayOfWeek);
        return schedules.stream()
                .flatMap(s -> Stream.iterate(
                        s.getStartTime(),
                        t -> t.isBefore(s.getEndTime()),
                        t -> t.plusMinutes(s.getIntervalMinute())
                ))
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional
    public void upsert(Store store, DayOfWeek dayOfWeek, List<ScheduleUpsertRequest> dtos){
        List<Schedule> schedules = findDaySchedules(store.getId(), dayOfWeek);

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

    //특정 요일의 예약 시간대 확인
    public void validateTime(Long storeId, LocalDateTime targetDateTime){
        if(targetDateTime.isBefore(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.RESERVATION_UNAVAILABLE_TIME);

        Schedule schedule = scheduleRepository.findDaySchedule(storeId, targetDateTime.getDayOfWeek(), targetDateTime.toLocalTime(), ScheduleStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_UNAVAILABLE_TIME));

        long diff = java.time.Duration.between(schedule.getStartTime(), targetDateTime.toLocalTime()).toMinutes();
        if(diff % schedule.getIntervalMinute() != 0)
            throw new BusinessException(ErrorCode.RESERVATION_UNAVAILABLE_TIME);
    }
}
