package com.example.demo.domain.menu.service;

import com.example.demo.domain.menu.domain.Menu;
import com.example.demo.domain.menu.domain.MenuStatus;
import com.example.demo.domain.menu.dto.MenuRegisterRequest;
import com.example.demo.domain.menu.dto.MenuRegisterResponse;
import com.example.demo.domain.menu.repository.MenuRepository;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public MenuRegisterResponse register(Long userId, Long storeId, MenuRegisterRequest dto){
        Store store = storeRepository.findByIdAndOwnerId(storeId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        Optional<Menu> existing = menuRepository.findByStoreIdAndName(storeId, dto.name());

        if(existing.isPresent())
            throw new BusinessException(ErrorCode.MENU_ALREADY_EXIST);

        Menu menu = Menu.builder()
                .store(store)
                .name(dto.name())
                .price(dto.price())
                .limitCount(dto.limitCount())
                .status(MenuStatus.ACTIVE)
                .build();

        menuRepository.save(menu);

        return MenuRegisterResponse.from(menu);
    }
}
