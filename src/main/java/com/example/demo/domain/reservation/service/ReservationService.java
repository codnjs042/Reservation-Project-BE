package com.example.demo.domain.reservation.service;

import com.example.demo.domain.menu.domain.Menu;
import com.example.demo.domain.menu.repository.MenuRepository;
import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import com.example.demo.domain.reservation.dto.ReservationAvailableResponse;
import com.example.demo.domain.reservation.dto.ReservationWhoWhenRequest;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public List<ReservationAvailableResponse> reserveWhen(User user, Long storeId, ReservationWhoWhenRequest dto){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        int headCount = dto.headCount();

        List<StoreTable> tables = storeTableRepository.findBySeat(storeId, headCount, headCount);

        List<ReservationAvailableResponse> slotTimes = new ArrayList<>();
        int interval = store.getSlotInterval();

        if(tables.isEmpty())
            return new ArrayList<>();

        else{
            List<Schedule> schedules = scheduleRepository.findByStoreIdAndDayOfWeekAndType(storeId, dto.targetDate().getDayOfWeek(), ScheduleType.OPEN);

            for(Schedule schedule: schedules){
                LocalTime slotTime = schedule.getStartTime();
                while(slotTime.isBefore(schedule.getEndTime())){
                    boolean isAvailable = checkTime(slotTime, headCount);
                    slotTimes.add(new ReservationAvailableResponse(slotTime, isAvailable));
                    slotTime = slotTime.plusMinutes(interval);
                }
            }
        }
        return slotTimes;
    }
}
