package com.planus.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planus.common.exception.NeedNicknameException;
import com.planus.entity.User;
import com.planus.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User login(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new NeedNicknameException(email);
        }

        return user.get();
    }

    public void setNicknameAndRegister(String email, String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);

        if (user.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        String password = UUID.randomUUID().toString();

        userRepository.save(User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build());
    }
}