package com.painting.canvas.member.util;

import com.painting.canvas.config.jwt.JwtFilter;
import com.painting.canvas.config.jwt.tokenDto.TokenDto;
import org.springframework.http.HttpHeaders;


public class MemberUtil {
    public static final Long LEAVE_MEMBER_ID = Long.MAX_VALUE;

    public static HttpHeaders getTokenHeaders(TokenDto tokenDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtFilter.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
        headers.add(JwtFilter.REFRESH_TOKEN_HEADER, tokenDto.getRefreshToken());

        return headers;
    }
}
