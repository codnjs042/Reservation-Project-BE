package com.example.demo.domain.schedule.repository;

import com.example.demo.domain.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
