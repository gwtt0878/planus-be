package com.planus.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planus.dto.UserCreateRequestDto;
import com.planus.dto.UserLoginRequestDto;
import com.planus.entity.User;
import com.planus.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
        User user = userService.createUser(requestDto);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginRequestDto requestDto,
            HttpSession httpSession) {
        Long id = userService.login(requestDto);
        httpSession.setAttribute("userId", id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "로그인 성공");
        response.put("userId", id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.ok().body("로그아웃 성공");
    }
}