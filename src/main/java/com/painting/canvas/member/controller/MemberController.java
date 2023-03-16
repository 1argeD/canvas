package com.painting.canvas.member.controller;

import com.painting.canvas.config.securityConfig.UserDetailsImpl;
import com.painting.canvas.member.Dto.MemberDto;
import com.painting.canvas.member.Dto.MemberInfoRequestDto;
import com.painting.canvas.member.Dto.SocialUserDto;
import com.painting.canvas.member.service.GoogleService;
import com.painting.canvas.member.service.MemberService;
import com.painting.canvas.member.util.MemberUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/member/logout")
    public ResponseEntity<?>logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.logout(userDetails);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/member/update")
    public ResponseEntity<?> updateMemberInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @Valid@RequestBody MemberInfoRequestDto requestDto) {
        MemberDto memberDto = memberService.updateMemberInfo(userDetails, requestDto);
        return ResponseEntity.ok(memberDto);
    }
}
