package com.example.demo.domain.reservation.service;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.dto.*;
import com.example.demo.domain.reservation.repository.ReservationRepository;
import com.example.demo.domain.schedule.repository.ScheduleRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.dto.StoreTableUpdateRequest;
import com.example.demo.domain.storeTable.repository.StoreTableRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Optional<StoreTable> findTable(Long storeId, LocalDateTime targetDateTime, int headCount,  List<StoreTable> tables){
        List<Reservation> reservations = reservationRepository.findByStoreIdAndTargetDateTimeAndStatus(storeId, targetDateTime, ReservationStatus.CONFIRMED);

        Map<Long, Long> reservationMap = reservations.stream()
                .collect(Collectors.groupingBy(r->r.getStoreTable().getId(), Collectors.counting()));

        return tables.stream()
                .sorted(Comparator.comparingInt(StoreTable::getMaxCapacity))
                .filter(t -> reservationMap.getOrDefault(t.getId(), 0L) < t.getCount())
                .findFirst();
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

    @Transactional
    public void rejectReservation(Long userId, Long reservationId) {
        Reservation reservation = findById(reservationId);

        if (!reservation.getStore().getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        LocalDateTime deadline = reservation.getTargetDateTime().toLocalDate().atStartOfDay();

        if (deadline.isBefore(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);

        reservation.updateStatus(ReservationStatus.REJECTED);
    }

    @Transactional
    public void cancelReservation(Long userId, Long reservationId){
        Reservation reservation = findById(reservationId);

        if(!reservation.getUser().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        LocalDateTime deadline = reservation.getTargetDateTime().toLocalDate().atStartOfDay();

        if(deadline.isBefore(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);

        reservation.updateStatus(ReservationStatus.CANCELED);
    }

    @Transactional
    public void visitedReservation(Long userId, Long reservationId){
        Reservation reservation = findById(reservationId);

        if(!reservation.getStore().getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        if(reservation.getTargetDateTime().isAfter(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);

        reservation.updateStatus(ReservationStatus.VISITED);
    }

    @Transactional
    public void noShowReservation(Long userId, Long reservationId){
        Reservation reservation = findById(reservationId);

        if(!reservation.getStore().getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        if(reservation.getTargetDateTime().isAfter(LocalDateTime.now()))
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);

        reservation.updateStatus(ReservationStatus.NO_SHOW);
    }

    public boolean hasFutureReservation(Long storeTableId){
        return reservationRepository.hasFutureReservation(LocalDateTime.now(), storeTableId, ReservationStatus.CONFIRMED);
    }

    public List<Long> countFutureReservation(Long storeTableId){
        return reservationRepository.countFutureReservation(LocalDateTime.now(), storeTableId, ReservationStatus.CONFIRMED);
    }
}
