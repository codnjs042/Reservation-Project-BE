package com.example.demo.domain.storeTable.controller;

import com.example.demo.domain.menu.dto.MenuRegisterResponse;
import com.example.demo.domain.storeTable.dto.StoreTableRegisterRequest;
import com.example.demo.domain.storeTable.service.StoreTableService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="StoreTable API", description ="가게 테이블 관리 API")
@RestController
@RequestMapping("/store/{storeId}/table")
@RequiredArgsConstructor
public class StoreTableController {
    private final StoreTableService storeTableService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody StoreTableRegisterRequest dto,
                                           @PathVariable Long storeId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails){
        storeTableService.register(userDetails.getId(), storeId, dto);

        return ResponseEntity.ok("완료");
    }
}
