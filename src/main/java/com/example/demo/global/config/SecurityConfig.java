package com.example.demo.global.config;

import com.example.demo.global.auth.CustomOAuth2UserService;
import com.example.demo.global.auth.OAuth2LoginSuccessHandler;
import com.example.demo.global.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    @Value("${swagger.enabled:false}")
    private boolean swaggerEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth
                        .requestMatchers("/admin/**", "/actuator/**").hasRole("ADMIN")
                        .requestMatchers("/stores/*/tables/**", "/owners/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT,"/stores/*/schedules/*").hasRole("OWNER")
                        .requestMatchers("/users/me/**", "/favorites/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/stores", "/stores/*/reservations").authenticated()
                        .requestMatchers("/users/check-username", "/users/signup", "/users/login",
                                "/users/logout", "/auth/login", "/auth/refresh",
                                "/auth/logout", "/area", "/error").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").access(
                                (authentication, context) -> new org.springframework.security.authorization.AuthorizationDecision(swaggerEnabled)
                        )
                        .requestMatchers(HttpMethod.GET, "/stores/**").permitAll()
                        .anyRequest().authenticated();
                })
                .formLogin(login -> login.disable())
                .logout(logout -> logout.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("Exception : {}", authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 필요(401)");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Exception : {}", accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한 부족(403)");
                        })
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_OWNER
                ROLE_OWNER > ROLE_USER
                """);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(allowedOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
