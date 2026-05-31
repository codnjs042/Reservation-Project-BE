package com.example.demo.domain.favorite.controller;

import com.example.demo.domain.favorite.serivce.FavoriteFacade;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="Favorite API", description ="관심 가게 관리 API")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteFacade favoriteFacade;

    @Operation(summary="관심 가게 추가", description="현재 로그인 상태의 유저가 관심 있는 가게를 추가")
    @PostMapping("/{storeId}")
    public ResponseEntity<String> addFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId
    ){
        favoriteFacade.add(userDetails.getUser(), storeId);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="관심 가게 삭제", description="현재 로그인 상태의 유저가 관심 누른 가게를 해제")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId
    ){
        favoriteFacade.delete(userDetails.getUser(), storeId);
        return ResponseEntity.ok("완료");
    }
}
