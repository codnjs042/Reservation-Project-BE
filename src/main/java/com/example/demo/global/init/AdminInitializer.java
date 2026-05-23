package com.example.demo.global.init;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.domain.UserStatus;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.infra.culture.CultureClient;
import com.example.demo.global.infra.culture.CultureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;
    private final CultureClient cultureClient;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        User admin = getOrCreateAdmin();
        createStoresIfAbsent(admin);
    }

    private User getOrCreateAdmin() {
        if (!userRepository.existsByRoleAndDeletedVersion(UserRole.ADMIN, 0L)) {
            User admin = User.builder()
                    .username(adminUsername)
                    .nickname("관리자")
                    .password(passwordEncoder.encode(adminPassword))
                    .loginType(UserLoginType.LOCAL)
                    .role(UserRole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);
            log.info("[AdminInitializer] 관리자 계정 생성 완료 (username: {})", adminUsername);
        } else {
            log.info("[AdminInitializer] 관리자 계정이 이미 존재합니다.");
        }

        return userRepository.findByUsernameAndDeletedVersion(adminUsername, 0L)
                .orElseThrow(() -> new IllegalStateException("관리자 계정 조회 실패"));
    }

    private void createStoresIfAbsent(User admin) {
        if (!storeRepository.getMyStores(admin.getId(), StoreStatus.SHUTDOWN).isEmpty()) {
            log.info("[AdminInitializer] 관리자 가게가 이미 존재합니다.");
            return;
        }

        List<CultureDto> cultureDtos = cultureClient.fetchCultureData("천안시", "한식");
        if (cultureDtos.isEmpty()) {
            log.warn("[AdminInitializer] CultureAPI 조회 결과 없음 — 가게 생성 생략");
            return;
        }

        for (int i = 0; i < cultureDtos.size(); i++) {
            CultureDto dto = cultureDtos.get(i);

            Double latitude = parseDouble(dto.latitude());
            Double longitude = parseDouble(dto.longitude());

            Store store = Store.builder()
                    .name(dto.name())
                    .category(StoreCategory.KOREAN)
                    .address(dto.address())
                    .detailAddress(null)
                    .zipcode("00000")
                    .sigunguCode(dto.sigunguCode())
                    .latitude(latitude)
                    .longitude(longitude)
                    .phone("00000000")
                    .owner(admin)
                    .ownerName("관리자")
                    .businessNumber(String.format("%010d", i + 1))
                    .status(StoreStatus.OPEN)
                    .build();

            storeRepository.save(store);
        }

        log.info("[AdminInitializer] 가게 {}건 생성 완료", cultureDtos.size());
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}