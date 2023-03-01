package com.painting.canvas.config.jwt;

import com.painting.canvas.config.jwt.tokenDto.TokenDto;
import com.painting.canvas.config.securityConfig.UserDetailsImpl;
import com.painting.canvas.member.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKet;
    public static String BEARER_PREFIX = "Bearer ";

    private final Long TokenValidTime = 60*60*1000L;
    private final Long RefreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L;

    private Key key;

    @PostConstruct
    protected void init() {
        secretKet = Base64.getEncoder().encodeToString(secretKet.getBytes());
        byte[] keyBates = Base64.getDecoder().decode(secretKet);
        key = Keys.hmacShaKeyFor(keyBates);
    }

    public TokenDto createToken(Member member) {
        long now = (new Date().getTime());
        Claims claims = Jwts.claims().setSubject(member.getId().toString());
        claims.put("role", member.getRole().toString());
        Date accessTokenExpiresIn = new Date(now + TokenValidTime);
        Date refreshTokenExpiresIn = new Date(now + RefreshTokenValidTime);

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
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken);
        return true;
    }
}

