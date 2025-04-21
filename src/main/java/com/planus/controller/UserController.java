package com.planus.controller;

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

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    public UserController(UserService userService, HttpSession httpSession) {
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequestDto requestDto) {
        User user = userService.createUser(requestDto);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto requestDto) {
        String id = userService.login(requestDto);
        httpSession.setAttribute("userId", id);

        return ResponseEntity.ok().body("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        userService.logout();
        return ResponseEntity.ok().body("로그아웃 성공");
    }
}