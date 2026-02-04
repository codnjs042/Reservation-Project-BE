package com.example.demo.domain.reservation.service;

import com.example.demo.domain.menu.domain.Menu;
import com.example.demo.domain.menu.repository.MenuRepository;
import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.dto.ReservationRequest;
import com.example.demo.domain.reservation.repository.ReservationRepository;
import com.example.demo.domain.reservationItem.domain.ReservationItem;
import com.example.demo.domain.reservationItem.domain.ReservationItemStatus;
import com.example.demo.domain.reservationItem.repository.ReservationItemRepository;
import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleType;
import com.example.demo.domain.schedule.repository.ScheduleRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.storeTable.domain.StoreTable;
import com.example.demo.domain.storeTable.repository.StoreTableRepository;
import com.example.demo.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ReservationItemRepository reservationItemRepository;
    private final ScheduleRepository scheduleRepository;
    private final StoreTableRepository storeTableRepository;

    public void reserve(User user, Long storeId, ReservationRequest dto){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        List<Schedule> schedules = scheduleRepository.findByStoreIdAndDayOfWeek(storeId, dto.targetDateTime().getDayOfWeek());

        if(schedules.isEmpty())
            throw new IllegalArgumentException("해당 가게의 운영 정보가 없습니다.");

        schedules.stream()
                .filter(s -> s.getType()== ScheduleType.OPEN)
                .filter(s -> !s.getStartTime().isAfter(dto.targetDateTime().toLocalTime())
                        && !s.getLastOrderTime().isBefore(dto.targetDateTime().toLocalTime()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("영업 시간에만 예약 가능합니다."));

        List<StoreTable> tables = storeTableRepository.findBySeat(storeId, dto.headCount());

        if(tables.isEmpty())
            throw new IllegalArgumentException("해당 가게의 테이블 최소 인원에 맞지 않습니다.");

        List<ReservationStatus> invalidStatus = List.of(ReservationStatus.CANCELED, ReservationStatus.REJECTED);

        List<Reservation> reservations = reservationRepository.findByStoreIdAndTargetDateTimeAndStatusNotIn(storeId, dto.targetDateTime(), invalidStatus);

        List<StoreTable> storeTables =  storeTableRepository.findByStoreId(storeId);

        Map<Long, Long > reservationMap = reservations.stream()
                .collect(Collectors.groupingBy(r->r.getStoreTable().getId(),
                        Collectors.counting()));

        // 테이블 최소인원보다 커야함
        // 최대인원보다 커도 남는 테이블 합쳐서 맞으면 됨

        Reservation reservation = Reservation.builder()
                .user(user)
                .store(store)
                .targetDateTime(dto.targetDateTime())
                .headCount(dto.headCount())
                .storeTable()
                .status(ReservationStatus.CONFIRMED)
                .build();

        reservationRepository.save(reservation);

        dto.items().forEach(itemDto-> {
           Menu menu = menuRepository.findById(itemDto.menuId())
                   .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

           ReservationItem reservationItem = ReservationItem.builder()
                   .reservation(reservation)
                   .menu(menu)
                   .price(menu.getPrice())
                   .count(itemDto.count())
                   .status(ReservationItemStatus.ACTIVE)
                   .build();

            reservationItemRepository.save(reservationItem);
        });
    }
}
