package com.example.demo.domain.reservation.service;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.dto.*;
import com.example.demo.domain.reservation.repository.ReservationRepository;
import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleType;
import com.example.demo.domain.schedule.repository.ScheduleRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.repository.StoreTableRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final ScheduleRepository scheduleRepository;
    private final StoreTableRepository storeTableRepository;

    @Transactional
    public List<ReservationTimeSlotResponse> getTimeSlot(Long storeId, ReservationTimeSlotRequest dto){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        List<StoreTable> tables = storeTableRepository.findBySeat(storeId, dto.headCount(), dto.headCount());

        List<ReservationTimeSlotResponse> slotTimes = new ArrayList<>();

        if(tables.isEmpty())
            return new ArrayList<>();

        else{
            List<Schedule> schedules = scheduleRepository.findByStoreIdAndDayOfWeekAndTypeOrderByStartTimeAsc(storeId, dto.targetDate().getDayOfWeek(), ScheduleType.OPEN);

            for(Schedule schedule: schedules){
                LocalTime slotTime = schedule.getStartTime();
                while(slotTime.isBefore(schedule.getEndTime())){
                    LocalDateTime targetDateTime = LocalDateTime.of(dto.targetDate(), slotTime);
                    boolean isAvailable = findTable(storeId, targetDateTime, dto.headCount(), tables).isPresent();
                    slotTimes.add(new ReservationTimeSlotResponse(slotTime, isAvailable));
                    slotTime = slotTime.plusMinutes(store.getSlotInterval());
                }
            }
        }
        return slotTimes;
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

    @Transactional
    public ReservationCreateResponse reserve(User user, Long storeId, ReservationCreateRequest dto){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        List<Schedule> schedules = scheduleRepository.findByStoreIdAndDayOfWeekAndTypeOrderByStartTimeAsc(storeId, dto.targetDateTime().getDayOfWeek(), ScheduleType.OPEN);

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

        List<StoreTable> tables = storeTableRepository.findBySeat(storeId, dto.headCount(), dto.headCount());

        StoreTable storeTable = findTable(storeId, dto.targetDateTime(), dto.headCount(), tables)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_FULL_TIME));

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

        return ReservationCreateResponse.from(reservation);
    }

    public List<ReservationSearchOwnerResponse> getStoreReservation(Long userId, Long storeId, ReservationSearchOwnerRequest dto){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        if(!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        LocalDate startDate = dto.startDate()==null ? LocalDate.now() : dto.startDate();
        LocalDate endDate = dto.endDate()==null ? LocalDate.now() : dto.endDate();

        List<ReservationStatus> statuses = (dto.status()==null || dto.status().isEmpty()) ? List.of(ReservationStatus.CONFIRMED, ReservationStatus.VISITED) : dto.status();

        return reservationRepository.getStoreReservationList(dto.type(), dto.keyword(), store.getId(), startDate, endDate, statuses).stream()
                .map(ReservationSearchOwnerResponse::from)
                .toList();
    }

    @Transactional
    public void rejectReservation(Long userId, Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if(!reservation.getStore().getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        reservation.updateStatus(ReservationStatus.REJECTED);
    }

    public List<ReservationSearchUserResponse> getMyReservation(Long userId, ReservationSearchUserRequest dto){
        LocalDate startDate = dto.startDate()==null ? LocalDate.now() : dto.startDate();
        LocalDate endDate = dto.endDate()==null ? LocalDate.now() : dto.endDate();

        List<ReservationStatus> statuses = (dto.status()==null || dto.status().isEmpty()) ? List.of(ReservationStatus.CONFIRMED, ReservationStatus.VISITED) : dto.status();

        return reservationRepository.getMyReservationList(dto.type(), dto.keyword(), startDate, endDate, statuses).stream()
                .map(ReservationSearchUserResponse::from)
                .toList();
    }

    @Transactional
    public void cancelReservation(Long userId, Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if(reservation.getUser().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        reservation.updateStatus(ReservationStatus.CANCELED);
    }

    @Transactional
    public void visitedReservation(Long userId, Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if(reservation.getUser().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        reservation.updateStatus(ReservationStatus.VISITED);
    }

    @Transactional
    public void noShowReservation(Long userId, Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if(reservation.getUser().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        reservation.updateStatus(ReservationStatus.NO_SHOW);
    }
}
