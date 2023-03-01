package com.painting.canvas.member.service;

import com.painting.canvas.config.jwt.JwtTokenProvider;
import com.painting.canvas.config.jwt.refresh.RefreshToken;
import com.painting.canvas.config.jwt.refresh.RefreshTokenRepository;
import com.painting.canvas.config.jwt.tokenDto.TokenDto;
import com.painting.canvas.member.model.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    public TokenDto saveToken(Member member) {

        TokenDto tokenDto = jwtTokenProvider.createToken(member);

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .tokenValue(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return tokenDto;
    }
}
