package com.example.demo.global.security;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserStatus;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    private User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    public Long getId(){
        return user.getId();
    }

    @Override
    @NonNull
    public String getUsername(){
        return user.getEmail();
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
}
