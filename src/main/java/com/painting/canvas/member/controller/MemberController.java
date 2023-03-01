package com.painting.canvas.member.controller;

import com.painting.canvas.member.Dto.GoogleUserInfo;
import com.painting.canvas.member.Dto.GoogleUserInfoDto;
import com.painting.canvas.member.Dto.SocialUserDto;
import com.painting.canvas.member.service.GoogleService;
import com.painting.canvas.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.MemberUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final GoogleService googleService;

    @GetMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestParam(value = "code") String code) throws IOException {
        SocialUserDto socialUserDto = googleService.googleLogin(code);
        return ResponseEntity.ok()
                .header(MemberUtils.getTokenHeaders(socialUserDto.getTokenDto()))
                .body(socialUserDto.getMemberDto());

    }

}
