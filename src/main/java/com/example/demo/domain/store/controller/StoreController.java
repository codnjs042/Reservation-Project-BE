package com.example.demo.domain.store.controller;

import com.example.demo.domain.store.dto.StoreRegisterRequest;
import com.example.demo.domain.store.dto.StoreRegisterResponse;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
