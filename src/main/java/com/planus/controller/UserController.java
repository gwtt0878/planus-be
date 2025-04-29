package com.planus.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planus.auth.JwtUtil;
import com.planus.auth.UserContext;
import com.planus.common.annotations.LoginRequired;
import com.planus.dto.UserCreateRequestDto;
import com.planus.dto.UserDetailResponseDto;
import com.planus.dto.UserLoginRequestDto;
import com.planus.dto.UserSearchResultResponseDto;
import com.planus.entity.User;
import com.planus.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/search")
    public ResponseEntity<UserSearchResultResponseDto> searchUserByNickname(@RequestParam String nickname) {
        List<User> users = userService.searchUserByNickname(nickname);
        UserSearchResultResponseDto responseDto = UserSearchResultResponseDto.of(users);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
        userService.createUser(requestDto);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        User user = userService.login(requestDto);

        String token = jwtUtil.createUserToken(user);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    @LoginRequired
    public ResponseEntity<String> logout() {
        UserContext.clearUserId();
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    @GetMapping("/myinfo")
    @LoginRequired
    public ResponseEntity<UserDetailResponseDto> getMyInfo() {
        Long userId = UserContext.getUserId();
        User user = userService.getUser(userId);
        UserDetailResponseDto responseDto = UserDetailResponseDto.from(user);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponseDto> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        UserDetailResponseDto responseDto = UserDetailResponseDto.from(user);

        return ResponseEntity.ok().body(responseDto);
    }
}