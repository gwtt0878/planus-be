package com.planus.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.planus.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSearchResultResponseDto {
    private List<UserInfoDto> users;

    public static UserSearchResultResponseDto of(List<User> users) {
        return UserSearchResultResponseDto.builder()
                .users(users.stream().map(UserInfoDto::from).collect(Collectors.toList()))
                .build();
    }
}
