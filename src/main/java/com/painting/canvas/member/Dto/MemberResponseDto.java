package com.painting.canvas.member.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private String nickname;
    private String email;
}
