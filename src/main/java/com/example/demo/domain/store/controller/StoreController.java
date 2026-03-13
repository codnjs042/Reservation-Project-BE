package com.example.demo.domain.store.controller;

import com.example.demo.domain.store.dto.*;
import com.example.demo.domain.store.service.StoreFacade;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Store API", description ="가게 관리 API")
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreFacade storeFacade;

    @Operation(summary="가게 등록", description="입력 받은 가게 정보를 DB에 저장. 성공 시 가게 상세 정보 반환.")
    @PostMapping("/register")
    public ResponseEntity<StoreResponse> register(
            @RequestBody StoreRegisterRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        StoreResponse response = storeFacade.register(userDetails.getUser(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="가게 상세 정보", description="입력 받은 가게의 상세 정보 반환")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getDetail(@PathVariable Long storeId){

        StoreResponse response = storeService.getDetail(storeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 목록 조회", description="현재 활성화 상태의 가게 목록 반환")
    @GetMapping
    public ResponseEntity<List<StoreSearchResponse>> getList(
            @ModelAttribute StoreSearchRequest dto){

        List<StoreSearchResponse> response = storeService.getList(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 정보 수정(기본 정보)", description="입력 받은 정보를 db에 업데이트")
    @PatchMapping("/{storeId}")
    public ResponseEntity<String> modify(
            @RequestBody StoreUpdateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        storeService.modify(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="가게 삭제", description="입력 받은 가게를 비활성화 상태로 변경")
    @PatchMapping("/{storeId}/delete")
    public ResponseEntity<String> delete(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        storeFacade.delete(userDetails.getId(), storeId);
        return ResponseEntity.ok("완료");
    }
}
