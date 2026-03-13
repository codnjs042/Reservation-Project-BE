package com.example.demo.domain.schedule.repository;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import com.example.demo.domain.schedule.domain.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("""
            select s from Schedule
            where s.store.id = :storeId
            and s.dayOfWeek = :dayOfWeek
            and s.status = :status
            """)
    List<Schedule> findDaySchedules(
            @Param("storeId") Long storeId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("status") ScheduleStatus status);

    List<Schedule> findByStoreIdAndDayOfWeekAndTypeOrderByStartTimeAsc(Long storeId, DayOfWeek dayOfWeek, ScheduleType type);
}
