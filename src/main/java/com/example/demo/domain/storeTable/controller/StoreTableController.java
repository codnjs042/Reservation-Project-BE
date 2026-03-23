package com.example.demo.domain.storeTable.controller;

import com.example.demo.domain.storeTable.dto.StoreTableCreateRequest;
import com.example.demo.domain.storeTable.dto.StoreTableResponse;
import com.example.demo.domain.storeTable.dto.StoreTableBulkUpdateRequest;
import com.example.demo.domain.storeTable.service.StoreTableFacade;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="StoreTable API", description ="가게 테이블 관리 API")
@RestController
@RequestMapping("/stores/{storeId}/tables")
@RequiredArgsConstructor
public class StoreTableController {
    private final StoreTableFacade storeTableFacade;

    @Operation(summary="테이블 초기 등록", description="입력 받은 테이블 정보를 DB에 저장")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody StoreTableCreateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        storeTableFacade.register(userDetails.getId(), storeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary="테이블 조회", description="입력 받은 가게의 테이블 정보 반환")
    @GetMapping
    public ResponseEntity<List<StoreTableResponse>> getTables(@PathVariable Long storeId)
    {
        List<StoreTableResponse> response = storeTableFacade.findAllTables(storeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="테이블 구성 변경", description="입력 받은 가게의 테이블 정보 일괄 수정")
    @PutMapping
    public ResponseEntity<String> adjust(
            @RequestBody StoreTableBulkUpdateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        storeTableFacade.adjust(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok("완료");
    }
}
