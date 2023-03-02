package com.painting.canvas.member.vaild;

import com.painting.canvas.member.model.Member;
import com.painting.canvas.member.repository.MemberRepository;


public class MemberValidator {

    public static Member validate(MemberRepository memberRepository, Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("아이디를 찾을 수 없음"));
    }
}
