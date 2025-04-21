package com.planus.service;

import org.springframework.stereotype.Service;

import com.planus.dto.UserCreateRequestDto;
import com.planus.dto.UserLoginRequestDto;
import com.planus.entity.User;
import com.planus.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public UserService(UserRepository userRepository, HttpSession httpSession) {
        this.userRepository = userRepository;
        this.httpSession = httpSession;
    }

    public User createUser(UserCreateRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();

        return userRepository.save(user);
    }

    public String login(UserLoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!user.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user.getId().toString();
    }

    public String logout() {
        httpSession.invalidate();
        return "Logout successful";
    }
}