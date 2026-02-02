package com.example.demo.domain.menu.controller;

import com.example.demo.domain.menu.dto.MenuRegisterRequest;
import com.example.demo.domain.menu.dto.MenuRegisterResponse;
import com.example.demo.domain.menu.service.MenuService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="Menu API", description ="메뉴 관리 API")
@RestController
@RequestMapping("/store/{storeId}/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/register")
    public ResponseEntity<MenuRegisterResponse> register(@RequestBody MenuRegisterRequest dto,
                                                         @PathVariable Long storeId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails){
        MenuRegisterResponse response = menuService.register(userDetails.getId(), storeId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
