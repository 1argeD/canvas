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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.jwtEncryptionKey}")
    private String secretKey;

    private static final String BEARER_PREFIX = "Bearer ";

    private final long tokenValidTime = 60 * 60 * 1000L; // 1 hour
    private final long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // 7 days

    private  SecretKey key;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
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

