package com.example.demo.domain.favorite.controller;

import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.favorite.dto.FavoriteResponse;
import com.example.demo.domain.favorite.serivce.FavoriteService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Favorite API", description ="관심 가게 관리 API")
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(summary="관심 가게 설정/해제", description="현재 로그인 상태의 유저가 관심 있는 가게를 설정/해제")
    @PatchMapping("/{storeId}")
    public ResponseEntity<String> toggleFavoirte(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId){
        favoriteService.toggleFavorite(userDetails.getUser(), storeId);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="관심 가게 목록 조회", description="현재 로그인 상태의 유저가 설정한 관심 있는 가게 목록 조회")
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails){
        List<FavoriteResponse> response = favoriteService.getList(userDetails.getId());
        return ResponseEntity.ok(response);
    }
}
