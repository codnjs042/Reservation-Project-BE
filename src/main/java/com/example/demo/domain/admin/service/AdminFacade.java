package com.example.demo.domain.admin.service;

import com.example.demo.domain.admin.dto.ReservationAdminRequest;
import com.example.demo.domain.admin.dto.ReservationAdminResponse;
import com.example.demo.domain.admin.dto.StoreAdminRequest;
import com.example.demo.domain.admin.dto.StoreAdminResponse;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFacade {
    private final UserService userService;
    private final StoreService storeService;
    private final ReservationService reservationService;

    public List<StoreAdminResponse> getStoresForAdmin(Long userId, StoreAdminRequest dto){
        User user = userService.findById(userId);

        if(user.getRole()!= UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        return storeService.getStoresForAdmin(dto.keyword(), dto.category(), dto.status());
    }

    public List<ReservationAdminResponse> getReservationsForAdmin(Long userId, ReservationAdminRequest dto){
        User user = userService.findById(userId);

        if(user.getRole()!= UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        return reservationService.getReservationsForAdmin(dto.keyword(), dto.status());
    }
}
