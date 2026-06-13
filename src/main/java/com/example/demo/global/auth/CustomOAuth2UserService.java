package com.example.demo.global.auth;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.domain.UserStatus;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String nickname = null;
        String email = null;
        UserLoginType loginType = null;
        String providerId = null;

        if("google".equals(registrationId)){
            email = oAuth2User.getAttribute("email");
            nickname = oAuth2User.getAttribute("name");
            loginType = UserLoginType.GOOGLE;
            providerId = oAuth2User.getName();
        }
        else if("kakao".equals(registrationId)){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            //log.info("attributes: {}", attributes);
            Map<String, Object> kakaoAcount = (Map<String, Object>) attributes.get("kakao_account");
            if(kakaoAcount != null){
                Map<String, Object> profile = (Map<String, Object>) kakaoAcount.get("profile");
                if(profile != null){
                    nickname = (String) profile.get("nickname");
                }
            }
            loginType = UserLoginType.KAKAO;
            Object id = attributes.get("id");
            if (id == null)
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_response"), "카카오 응답에서 id를 찾을 수 없습니다.");
            providerId = id.toString();
        }
        else if("naver".equals(registrationId)){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            //log.info("attributes: {}", attributes);
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if(response != null){
                nickname = (String) response.get("name");
                email = (String) response.get("email");
                providerId = (String) response.get("id");
            }
            loginType = UserLoginType.NAVER;
            if (providerId == null)
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_response"), "네이버 응답에서 id를 찾을 수 없습니다.");
        }

        final String finalEmail = email;
        final String finalNickName = nickname;
        final UserLoginType finalLoginType = loginType;
        final String finalProviderId = providerId;
        final String username = finalProviderId;

        User user = userRepository.findByUsernameAndDeletedVersion(username, 0L)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .username(username)
                                .nickname(finalNickName)
                                .email(finalEmail)
                                .loginType(finalLoginType)
                                .providerId(finalProviderId)
                                .role(UserRole.USER)
                                .status(UserStatus.ACTIVE)
                                .build()
                ));
        return new CustomUserDetails(user, oAuth2User.getAttributes(), finalProviderId);
    }
}
