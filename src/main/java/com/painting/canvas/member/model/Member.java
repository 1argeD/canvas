package com.painting.canvas.member.model;

import com.painting.canvas.member.Dto.GoogleUserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.lock.PessimisticReadUpdateLockingStrategy;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Member")
public class Member {
    @Id
    private String id;

    private String  googleId;

    private String  kakaoId;

    private String nickname;

    private String email;
    private Role role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public static Member of(GoogleUserInfoDto googleUserInfoDto) {
        return Member.builder()
                .nickname(googleUserInfoDto.getName())
                .id(googleUserInfoDto.getGoogleId())
                .build();
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void setMemberRole(Role role) {
        this.role = role;
    }
}
