package com.painting.canvas.member.Dto;

import com.painting.canvas.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor
@Document
public class GoogleUserInfoDto {
    private String googleId;
    private String name;
    private String email;
}
