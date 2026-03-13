package com.example.demo.domain.schedule.domain;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int intervalMinute;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @Builder
    public Schedule(Store store, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, int intervalMinute, ScheduleStatus status){
        this.store = store;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalMinute = intervalMinute;
        this.status = status;
    }

    public void updateStatus(ScheduleStatus status){
        this.status = status;
    }
}
