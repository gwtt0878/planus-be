package com.planus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planus.dto.UserCreateRequestDto;
import com.planus.dto.UserDetailResponseDto;
import com.planus.dto.UserLoginRequestDto;
import com.planus.dto.UserSearchResultResponseDto;
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

    @GetMapping("/search")
    public ResponseEntity<UserSearchResultResponseDto> searchUserByNickname(@RequestParam String nickname) {
        List<User> users = userService.searchUserByNickname(nickname);
        UserSearchResultResponseDto responseDto = UserSearchResultResponseDto.of(users);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
        User user = userService.createUser(requestDto);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginRequestDto requestDto,
            HttpSession httpSession) {
        User user = userService.login(requestDto);
        httpSession.setAttribute("userId", user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "로그인 성공");
        response.put("userId", user.getId());
        response.put("nickname", user.getNickname());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    @GetMapping("/myinfo")
    public ResponseEntity<UserDetailResponseDto> getMyInfo(HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
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