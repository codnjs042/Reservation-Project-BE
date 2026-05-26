package com.example.demo.domain.store.controller;

import com.example.demo.domain.store.dto.*;
import com.example.demo.domain.store.service.StoreFacade;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import java.util.List;

@Tag(name="Store API", description ="가게 관리 API")
@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreFacade storeFacade;

    @Operation(summary="가게 등록", description="현재 로그인한 유저 권한의 가게 등록.")
    @PostMapping
    public ResponseEntity<StoreDetailResponse> createStore(
            @Valid @RequestBody StoreCreateRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        StoreDetailResponse response = storeFacade.create(userDetails.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="가게 상세 정보", description="입력 받은 가게의 상세 정보 반환")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailResponse> getDetail(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long userId = (userDetails!=null) ? userDetails.getId() : null;
        StoreDetailResponse response = storeFacade.getDetail(userId, storeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 목록 조회", description="현재 활성화 상태의 가게 목록 반환")
    @GetMapping
    public ResponseEntity<Page<StoreResponse>> getList(
            @Valid @ModelAttribute StoreSearchRequest dto)
    {

        Page<StoreResponse> response = storeService.getList(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="내 위치 반경 3km 가게 목록", description="내 위치 반경 km 내 현재 활성화 상태의 가게 목록 반환")
    @GetMapping("/nearby")
    public ResponseEntity<List<StorePointResponse>> get3kmList(
            @Valid @ModelAttribute MyPointRequest dto)
    {
        List<StorePointResponse> response = storeService.get3kmList(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="인기 가게 TOP 6 조회", description="현재 활성화 상태의 관심수가 많은 가게 목록 반환")
    @GetMapping("/famous")
    public ResponseEntity<List<StoreResponse>> getFamous()
    {
        List<StoreResponse> response = storeService.getFamous();
        return ResponseEntity.ok(response);
    }
}
