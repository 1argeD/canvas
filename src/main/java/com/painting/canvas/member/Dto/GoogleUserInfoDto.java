package com.painting.canvas.member.Dto;

import com.painting.canvas.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GoogleUserInfoDto {
    private Long googleId;
    private String name;
    private String email;

    private GoogleUserInfoDto googleUserInfoDto(Member member) {
        this.googleId = member.getId();
        this.name = member.getNickname();
        this.email = member.getEmail();
        return this;
    }
}
