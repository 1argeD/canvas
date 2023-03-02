package com.painting.canvas.member.controller;

import com.painting.canvas.member.Dto.SocialUserDto;
import com.painting.canvas.member.service.GoogleService;
import com.painting.canvas.member.service.MemberService;
import com.painting.canvas.member.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final GoogleService googleService;

    @GetMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestParam(value = "code") String code) throws IOException {
        SocialUserDto socialUserDto = googleService.googleLogin(code);
        return ResponseEntity.ok()
                .headers(MemberUtil.getTokenHeaders(socialUserDto.getTokenDto()))
                .body(socialUserDto.getMemberDto());

    }

}
