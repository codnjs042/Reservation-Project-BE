package com.example.demo.global.security;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserStatus;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
    private User user;
    private Map<String, Object> attributes;
    private String providerId;

    public CustomUserDetails(User user){
        this.user = user;
    }

    public CustomUserDetails(User user, Map<String, Object> attributes, String providerId){
        this.user = user;
        this.attributes = attributes;
        this.providerId = providerId;
    }

    public Long getId(){
        return user.getId();
    }

    @Override
    @NonNull
    public String getUsername(){
        return user.getUsername();
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        String roleName = "ROLE_" + this.user.getRole().name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public boolean isEnabled(){
        return user.getStatus() != UserStatus.DELETED;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName(){
        return this.providerId;
    }
}
