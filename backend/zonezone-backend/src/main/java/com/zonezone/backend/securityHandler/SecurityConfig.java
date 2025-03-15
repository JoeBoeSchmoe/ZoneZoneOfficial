package com.zonezone.backend.securityHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Correct way to disable CSRF in Spring Security 6.1+
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/accountUsers/listUsers").permitAll()  // Allow listing users
                        .requestMatchers("/accountUsers/createUser").permitAll() // Allow user creation
                        .anyRequest().authenticated() // Other endpoints require authentication
                );

        return http.build();
    }
}