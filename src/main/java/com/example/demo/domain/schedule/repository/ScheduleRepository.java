package com.example.demo.domain.schedule.repository;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("""
            select s from Schedule s
            where s.store.id = :storeId
            and s.dayOfWeek = :dayOfWeek
            and s.status = :status
            """)
    List<Schedule> findDaySchedules(
            @Param("storeId") Long storeId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("status") ScheduleStatus status);

    @Query("""
            select s from Schedule s
            where s.store.id = :storeId
            and s.dayOfWeek = :dayOfWeek
            and s.startTime <= :targetTime
            and s.endTime > :targetTime
            and s.status = :status
            """)
    Optional<Schedule> findDaySchedule(
            @Param("storeId") Long storeId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("targetTime") LocalTime targetTime,
            @Param("status") ScheduleStatus status);
}
