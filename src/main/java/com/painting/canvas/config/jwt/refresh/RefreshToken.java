package com.painting.canvas.config.jwt.refresh;

import com.painting.canvas.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken {
    @Id
    @Column(nullable = false)
    private String id;

    @JoinColumn(name="memberId", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String tokenValue;

    private void updateToken(String token) {
        this.tokenValue = token;
    }

}
