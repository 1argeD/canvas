package com.painting.canvas.member.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor
@Document
public class MemberResponseDto {
    private String nickname;
    private String email;
}
