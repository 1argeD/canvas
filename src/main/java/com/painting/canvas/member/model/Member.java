package com.painting.canvas.member.model;

import com.painting.canvas.member.Dto.GoogleUserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.IdentifierLoadAccess;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("Member")
public class Member {
    @Id
    private String id;

    private String  googleId;

    private String  kakaoId;

    private String nickname;

    private String email;
    private Role role;

    public Member(String id, String googleId, String kakaoId, String email, Role role ) {
        super();
        this.id =id;
        this.googleId = googleId;
        this.kakaoId = kakaoId;
        this.email = email;
        this.role = role;
    }

    public static Member of(GoogleUserInfoDto googleUserInfoDto) {
        return Member.builder()
                .nickname(googleUserInfoDto.getName())
                .id(googleUserInfoDto.getGoogleId())
                .build();
    }

    public void update(String nickname ) {
        this.nickname = nickname;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void setMemberRole(Role role) {
        this.role = role;
    }
}
