package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("""
            select u from User u
            where (:keyword is null or :keyword='' or
                (cast(u.id as string) like %:keyword%) or
                (u.email like %:keyword%) or
                (u.nickname like %:keyword%))
            and (:loginType is null or u.loginType = :loginType)
            and (:role is null or u.role = :role)
            and (:status is null or u.status = :status)
            """)
    List<User> getUsersForAdmin(
            @Param("keyword") String keyword,
            @Param("loginType") UserLoginType loginType,
            @Param("role") UserRole role,
            @Param("status") UserStatus status);
}
