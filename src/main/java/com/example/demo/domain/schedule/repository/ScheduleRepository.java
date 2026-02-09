package com.example.demo.domain.schedule.repository;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByStoreIdAndDayOfWeekAndType(Long storeId, DayOfWeek dayOfWeek, ScheduleType type);
}
