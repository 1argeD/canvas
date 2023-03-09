package com.painting.canvas.config.jwt;

import com.painting.canvas.config.jwt.tokenDto.TokenDto;
import com.painting.canvas.config.securityConfig.UserDetailsImpl;
import com.painting.canvas.member.model.Member;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String SECRET_KEY="87SD980AGG45r3HFDH321D4D4H5D32AS6236H7D56AGFDGW";

    private static final String BEARER_PREFIX = "Bearer ";

    private final long tokenValidTime = 60 * 60 * 1000L; // 1 hour
    private final long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // 7 days

    private  SecretKey key;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(Member member) {
        long now = System.currentTimeMillis();
        Claims claims = Jwts.claims().setSubject(member.getId().toString());
        claims.put("role", member.getRole().toString());
        Date accessTokenExpiresIn = new Date(now + tokenValidTime);
        Date refreshTokenExpiresIn = new Date(now + refreshTokenValidTime);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .accessToken(BEARER_PREFIX + accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Claims getClaims(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
    }

    public Authentication getAuthentication(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        UserDetailsImpl userDetails = new UserDetailsImpl(Long.parseLong(claims.getSubject()), claims.get("role").toString());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

