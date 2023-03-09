package com.painting.canvas.member.Dto;

import com.painting.canvas.config.jwt.tokenDto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class SocialUserDto {
    private MemberDto memberDto;
    private TokenDto tokenDto;
}
