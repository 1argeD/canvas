package com.painting.canvas.member.service;

import com.painting.canvas.config.jwt.JwtTokenProvider;
import com.painting.canvas.config.jwt.refresh.RefreshToken;
import com.painting.canvas.config.jwt.refresh.RefreshTokenRepository;
import com.painting.canvas.config.jwt.tokenDto.TokenDto;
import com.painting.canvas.config.securityConfig.UserDetailsImpl;
import com.painting.canvas.member.Dto.MemberDto;
import com.painting.canvas.member.Dto.MemberInfoRequestDto;
import com.painting.canvas.member.model.Member;
import com.painting.canvas.member.repository.MemberRepository;
import com.painting.canvas.member.vaild.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(UserDetailsImpl userDetails) {
        refreshTokenRepository.deleteByMember_Id(userDetails.getMemberId());
    }
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

    @Transactional
    public MemberDto updateMemberInfo(UserDetailsImpl userDetails, MemberInfoRequestDto requestDto) {
        Member member = MemberValidator.validate(memberRepository, userDetails.getMemberId());
        member.update(requestDto.getNickname());
        return MemberDto.of(member);
    }
}
