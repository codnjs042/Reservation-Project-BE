package com.example.demo.domain.store.controller;

import com.example.demo.domain.store.dto.*;
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

    @Operation(summary="가게 등록", description="입력 받은 가게 정보를 DB에 저장. 성공 시 가게 정보 반환.")
    @PostMapping("/register")
    public ResponseEntity<StoreRegisterResponse> register(
            @RequestBody StoreRegisterRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        StoreRegisterResponse response = storeService.register(userDetails.getUser(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="가게 목록 조회", description="가게 목록 조회")
    @GetMapping
    public ResponseEntity<List<StoreSearchResponse>> getList(
            @ModelAttribute StoreSearchRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        List<StoreSearchResponse> response = storeService.getList(userDetails.getId(), dto);

        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 정보 일괄 수정", description="가게 정보 일괄 수정")
    @PatchMapping("/{storeId}")
    public ResponseEntity<String> modify(
            @RequestBody StoreUpdateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        storeService.modify(userDetails.getId(), storeId, dto);

        return ResponseEntity.ok("완료");
    }

    @Operation(summary="가게 삭제", description="가게 삭제")
    @PatchMapping("/{storeId}/delete")
    public ResponseEntity<String> delete(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        storeService.delete(userDetails.getId(), storeId);

        return ResponseEntity.ok("완료");
    }
}
