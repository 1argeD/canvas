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
import lombok.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class GoogleService {
    String googleClientId = "589747262012-g37na5ksf8rkgr327p8a5omeppj7l7fo.apps.googleusercontent.com";

    String googleClientSecret = "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";

    String googleRedirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=589747262012-g37na5ksf8rkgr327p8a5omeppj7l7fo.apps.googleusercontent.com&response_type=token&redirect_uri=https://localhost:3000&scope=https://www.googleapis.com/auth/userinfo.email";
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
        ResponseEntity<String > response = rt.exchange("https://www.googleapis.com/oauth2/v2/userinfo?access_token=", HttpMethod.POST, googleInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("properties").get("email").asText();
        return new GoogleUserInfoDto(id.toString(), nickname, email);
    }

    Member signupGoogle(GoogleUserInfoDto googleUserInfoDto) {
        String googleId = googleUserInfoDto.getGoogleId();
        if(memberRepository.findAllByGoogleId(googleId)!=null) {
            return memberRepository.findAllByGoogleId(googleId);
        } else {
            Member googleUser = Member.of(googleUserInfoDto);
            googleUser.setMemberRole(Role.USER);
            return memberRepository.save(googleUser);
        }
    }
}
