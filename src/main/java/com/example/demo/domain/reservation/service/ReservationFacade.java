package com.example.demo.domain.reservation.service;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.dto.*;
import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleType;
import com.example.demo.domain.schedule.service.ScheduleService;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.service.StoreTableService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationFacade {
    private final ReservationService reservationService;
    private final StoreService storeService;
    private final ScheduleService scheduleService;
    private final StoreTableService storeTableService;

    @Transactional
    public ReservationCreateResponse reserve(User user, Long storeId, ReservationCreateRequest dto){
        Store store = storeService.findById(storeId);

        List<Schedule> schedules = scheduleService.findDaySchedule(storeId, dto.targetDateTime().getDayOfWeek());

        LocalTime time = dto.targetDateTime().toLocalTime();
        boolean isAvailable = false;

        for(Schedule schedule: schedules){
            if(!time.isBefore(schedule.getStartTime()) && time.isBefore(schedule.getEndTime())) {
                if (ChronoUnit.MINUTES.between(schedule.getStartTime(), time) % store.getSlotInterval() == 0) {
                    isAvailable = true;
                    break;
                }
            }
            else
                break;
        }

        if(!isAvailable)
            throw new BusinessException(ErrorCode.RESERVATION_UNAVAILABLE_TIME);

        List<StoreTable> tables = storeTableService.findBySeatWithLock(storeId, dto.headCount());

        StoreTable storeTable = reservationService.findTable(storeId, dto.targetDateTime(), dto.headCount(), tables)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_FULL_TIME));

        Reservation reservation = reservationService.register(user, store, storeTable, dto);

        return ReservationCreateResponse.from(reservation);
    }

    @Transactional
    public List<ReservationTimeSlotResponse> getTimeSlot(Long storeId, ReservationTimeSlotRequest dto) {
        Store store = storeService.findById(storeId);

        List<StoreTable> tables = storeTableService.findBySeat(storeId, dto.headCount());

        List<ReservationTimeSlotResponse> slotTimes = new ArrayList<>();

        if (tables.isEmpty())
            return new ArrayList<>();

        else {
            List<Schedule> schedules = scheduleService.findDaySchedule(storeId, dto.targetDate().getDayOfWeek());

            for (Schedule schedule : schedules) {
                LocalTime slotTime = schedule.getStartTime();
                while (slotTime.isBefore(schedule.getEndTime())) {
                    LocalDateTime targetDateTime = LocalDateTime.of(dto.targetDate(), slotTime);
                    boolean isAvailable = reservationService.findTable(storeId, targetDateTime, dto.headCount(), tables).isPresent();
                    slotTimes.add(new ReservationTimeSlotResponse(slotTime, isAvailable));
                    slotTime = slotTime.plusMinutes(store.getSlotInterval());
                }
            }
        }
        return slotTimes;
    }

    public List<ReservationSearchOwnerResponse> getStoreReservation(Long userId, Long storeId, ReservationSearchOwnerRequest dto){
        Store store = storeService.findById(storeId);

        if(!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        LocalDate startDate = dto.startDate()==null ? LocalDate.now() : dto.startDate();
        LocalDate endDate = dto.endDate()==null ? LocalDate.now() : dto.endDate();

        List<ReservationStatus> statuses = (dto.status()==null || dto.status().isEmpty()) ? List.of(ReservationStatus.CONFIRMED, ReservationStatus.VISITED) : dto.status();

        List<Reservation> reservations =  reservationService.getStoreReservation(store.getId(), dto, startDate, endDate, statuses);

        return reservations.stream()
                .map(ReservationSearchOwnerResponse::from)
                .toList();
    }
}
