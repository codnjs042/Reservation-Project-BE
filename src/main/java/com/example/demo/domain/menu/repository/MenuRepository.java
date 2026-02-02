package com.example.demo.domain.menu.repository;

import com.example.demo.domain.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByStoreIdAndName(Long storeId, String name);
}
