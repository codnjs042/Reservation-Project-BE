package com.example.demo.domain.menu.dto;

import com.example.demo.domain.menu.domain.Menu;

public record MenuRegisterResponse(Long id,
                                   String name) {
    public static MenuRegisterResponse from(Menu menu){
        return new MenuRegisterResponse(
                menu.getId(),
                menu.getName()
        );
    }
}
