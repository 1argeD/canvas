package com.painting.canvas.config.auth;

import com.painting.canvas.member.Member;
import com.painting.canvas.member.Role;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.engine.spi.ManagedEntity;

import java.util.Map;
import java.util.Objects;

@Getter
public class OAuthAttributes {
    private Map<String , Objects> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String , Objects> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Objects> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Objects> attributes) {
        return OAuthAttributes.builder()
                .name(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(name)
                .email(email)
                .role(Role.USER)
                .build();
        }
}
