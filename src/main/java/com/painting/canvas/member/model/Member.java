package com.painting.canvas.member.model;

import com.painting.canvas.member.Dto.GoogleUserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long googleId;

    @Column(unique = true)
    private Long kakaoId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    public static Member of(GoogleUserInfoDto googleUserInfoDto) {
        return Member.builder()
                .nickname(googleUserInfoDto.getName())
                .id(googleUserInfoDto.getGoogleId())
                .build();
    }

    public Member update(String nickname ) {
        this.nickname = nickname;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void setMemberRole(Role role) {
        this.role = role;
    }
}
