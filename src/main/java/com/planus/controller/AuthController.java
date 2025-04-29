package com.planus.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.planus.auth.JwtUtil;
import com.planus.entity.User;
import com.planus.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.client.redirect-uri}")
    private String googleRedirectUri;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${client.host}")
    private String clientHost;

    private final AuthService authService;

    @GetMapping("/login/google")
    public ResponseEntity<String> login() {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri + "&response_type=code&scope=openid%20email%20profile";

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(googleLoginUrl)).build();
    }

    @GetMapping("/callback/google")
    public ResponseEntity<Map<String, String>> callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error) throws IOException {

        if (error != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(clientHost + "/login"))
                    .build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("redirect_uri", googleRedirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                requestEntity,
                Map.class);

        String accessToken = response.getBody().get("access_token").toString();

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        userInfoHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                userInfoRequestEntity,
                Map.class);

        String email = userInfoResponse.getBody().get("email").toString();

        User user = authService.login(email);

        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getNickname());

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(clientHost + "/login/oauth2redirect?token=" + token))
                .build();
    }

    @PostMapping("/nickname")
    public ResponseEntity<String> setNicknameAndRegister(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String nickname = request.get("nickname");

        authService.setNicknameAndRegister(email, nickname);

        return ResponseEntity.ok("회원가입 성공");
    }
}
