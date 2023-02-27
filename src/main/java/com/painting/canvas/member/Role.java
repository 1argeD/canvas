package com.painting.canvas.member;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    Guest("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반_사용자");

    private final String key;
    private final String title;
}
