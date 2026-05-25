package com.example.demo.domain.admin.service;

import com.example.demo.domain.admin.dto.ReservationAdminDetailResponse;
import com.example.demo.domain.admin.dto.ReservationAdminRequest;
import com.example.demo.domain.admin.dto.ReservationAdminResponse;
import com.example.demo.domain.admin.dto.StoreAdminDetailResponse;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFacade {
    private final UserService userService;
    private final StoreService storeService;
    private final ReservationService reservationService;

    public StoreAdminDetailResponse getStoreForAdmin(Long adminId, Long storeId){
        User user = userService.findById(adminId);

        if(user.getRole() != UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        return StoreAdminDetailResponse.from(storeService.findById(storeId));
    }

    public ReservationAdminDetailResponse getReservationForAdmin(Long adminId, Long reservationId){
        User user = userService.findById(adminId);

        if(user.getRole() != UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        return reservationService.findByIdForAdmin(reservationId);
    }

    public Page<StoreAdminResponse> getStoresForAdmin(Long userId, StoreAdminRequest dto){
        User user = userService.findById(userId);

        if(user.getRole()!= UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        PageRequest pageable = PageRequest.of(dto.page(), dto.size(), Sort.by("id").descending());
        return storeService.getStoresForAdmin(dto.keyword(), dto.category(), dto.status(), pageable);
    }

    public Page<ReservationAdminResponse> getReservationsForAdmin(Long userId, ReservationAdminRequest dto){
        User user = userService.findById(userId);

        if(user.getRole()!= UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        PageRequest pageable = PageRequest.of(dto.page(), dto.size(), Sort.by("id").descending());
        return reservationService.getReservationsForAdmin(dto.keyword(), dto.status(), pageable);
    }
}
