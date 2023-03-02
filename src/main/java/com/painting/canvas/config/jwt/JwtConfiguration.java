package com.painting.canvas.config.jwt;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtConfiguration {
    private final JwtTokenProvider jwtTokenProvider;

    public void configure(HttpSecurity httpSecurity) {
        jwtFilter customJwtFilter = new jwtFilter(jwtTokenProvider);
        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();

        httpSecurity.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtExceptionFilter, jwtFilter.class);
    }
}
