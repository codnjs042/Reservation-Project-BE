package com.example.demo.domain.reservation.service;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.dto.*;
import com.example.demo.domain.reservation.repository.ReservationRepository;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.domain.StoreTableStatus;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation register(User user, Store store, StoreTable storeTable, ReservationCreateRequest dto){
        Reservation reservation = Reservation.builder()
                .user(user)
                .name(dto.name())
                .store(store)
                .targetDateTime(dto.targetDateTime())
                .headCount(dto.headCount())
                .storeTable(storeTable)
                .status(ReservationStatus.CONFIRMED)
                .build();

        reservationRepository.save(reservation);

        return reservation;
    }

    public Reservation findById(Long reservationId){
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public Set<LocalTime> getFullSlots(Long storeId, LocalDate targetDate, int headCount){
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.atTime(LocalTime.MAX);

        return reservationRepository.findFullTimes(storeId, start, end, ReservationStatus.CONFIRMED, headCount, StoreTableStatus.ACTIVE)
                .stream()
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toSet());
    }

    public List<Reservation> getStoreReservation(Long storeId, ReservationSearchOwnerRequest dto, LocalDate startDate, LocalDate endDate, List<ReservationStatus> statuses){
        return reservationRepository.getStoreReservationList(dto.type(), dto.keyword(), storeId, startDate, endDate, statuses);
    }

    public List<ReservationSearchUserResponse> getMyReservation(Long userId, ReservationSearchUserRequest dto){
        LocalDate startDate = dto.startDate()==null ? LocalDate.now() : dto.startDate();
        LocalDate endDate = dto.endDate()==null ? LocalDate.now() : dto.endDate();

        List<ReservationStatus> statuses = (dto.status()==null || dto.status().isEmpty()) ? List.of(ReservationStatus.CONFIRMED, ReservationStatus.VISITED) : dto.status();

        return reservationRepository.getMyReservationList(userId, dto.type(), dto.keyword(), startDate, endDate, statuses).stream()
                .map(ReservationSearchUserResponse::from)
                .toList();
    }

    public void validateOwner(Reservation reservation, Long userId){
        if(!reservation.getStore().getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    public void validateGuest(Reservation reservation, Long userId){
        if(!reservation.getUser().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    public void validateBeforeReservation(Reservation reservation){
        LocalDateTime deadline = reservation.getTargetDateTime().toLocalDate().atStartOfDay();

        if (deadline.isBefore(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);

        if(reservation.getStatus()!=ReservationStatus.CONFIRMED)
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);
    }

    public void validateAfterReservation(Reservation reservation){
        if(reservation.getTargetDateTime().isAfter(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);
    }

    @Transactional
    public void rejectReservation(Long userId, Long reservationId) {
        Reservation reservation = findById(reservationId);
        validateOwner(reservation, userId);
        validateBeforeReservation(reservation);
        reservation.updateStatus(ReservationStatus.REJECTED);
    }

    @Transactional
    public void cancelReservation(Long userId, Long reservationId){
        Reservation reservation = findById(reservationId);
        validateGuest(reservation, userId);
        validateBeforeReservation(reservation);
        reservation.updateStatus(ReservationStatus.CANCELED);
    }

    @Transactional
    public void visitedReservation(Long userId, Long reservationId){
        Reservation reservation = findById(reservationId);
        validateOwner(reservation, userId);
        validateAfterReservation(reservation);
        reservation.updateStatus(ReservationStatus.VISITED);
    }

    @Transactional
    public void noShowReservation(Long userId, Long reservationId){
        Reservation reservation = findById(reservationId);
        validateOwner(reservation, userId);
        validateAfterReservation(reservation);
        reservation.updateStatus(ReservationStatus.NO_SHOW);
    }

    public void validateTime(Long storeId, DayOfWeek dayOfWeek){
        List<Reservation> reservations = reservationRepository.validateTime(storeId, ReservationStatus.CONFIRMED, dayOfWeek, ScheduleStatus.ACTIVE);
        if(!reservations.isEmpty())
            throw new BusinessException(ErrorCode.SCHEDULE_LOCKED);
    }

    public void validateCapacity(Long storeId, int minCapacity, int maxCapacity, String tableName){
        List<Reservation> reservations = reservationRepository.validateCapacity(storeId, minCapacity, maxCapacity, ReservationStatus.CONFIRMED, tableName, StoreTableStatus.ACTIVE);
        if(!reservations.isEmpty())
            throw new BusinessException(ErrorCode.TABLE_LOCKED);
    }
}
