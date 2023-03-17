package com.painting.canvas.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.painting.canvas.config.jwt.tokenDto.TokenDto;
import com.painting.canvas.member.Dto.GoogleUserInfoDto;
import com.painting.canvas.member.Dto.MemberDto;
import com.painting.canvas.member.Dto.SocialUserDto;
import com.painting.canvas.member.model.Member;
import com.painting.canvas.member.model.Role;
import com.painting.canvas.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;


@Service
@RequiredArgsConstructor
public class GoogleService {

    @Value("${client-id}")
    String googleClientId;

    @Value("${client-secret}")
    String googleClientSecret;

    @Value("${redirect-uri}")
    String googleRedirectUrl;
    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final String ACCESS_TOKEN_URL = googleRedirectUrl;

    String getGoogleAccessToken(String code) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String , String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", googleClientId);
        body.add("redirect_url", ACCESS_TOKEN_URL);
        body.add("code", code);

        HttpEntity<MultiValueMap<String , String>>googleTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://accounts.google.com/o/oauth2/v2/auth", HttpMethod.POST, googleTokenRequest, String.class);
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    @Transactional
    public SocialUserDto googleLogin(String code) throws IOException{
        String accessToken = getGoogleAccessToken(code);

        GoogleUserInfoDto googleUserInfoDto = getGoogleUserInfo(accessToken);

        Member googleUser = signupGoogle(googleUserInfoDto);

        TokenDto tokenDto = memberService.saveToken(googleUser);
        MemberDto memberDto = new MemberDto(googleUser.getId(), googleUser.getNickname(), googleUser.getEmail());
        return new SocialUserDto(memberDto, tokenDto);
    }

    public GoogleUserInfoDto getGoogleUserInfo(String  accessToken) throws IOException{
        HttpHeaders headers  = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String , String >> googleInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://www.googleapis.com/oauth2/v2/userinfo?access_token=", HttpMethod.POST, googleInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("properties").get("email").asText();
        return new GoogleUserInfoDto(Long.toString(id), nickname, email);
    }

    Member signupGoogle(GoogleUserInfoDto googleUserInfoDto) {
        String googleId = googleUserInfoDto.getGoogleId();
        return memberRepository.findAllByGoogleId(googleId)
                .orElseGet( () -> {
                Member googleUser = Member.of(googleUserInfoDto);
                googleUser.setMemberRole(Role.USER);
                return memberRepository.save(googleUser);
                });
    }
}
