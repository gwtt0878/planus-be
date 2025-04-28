package com.planus.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.planus.entity.Schedule;
import com.planus.entity.ScheduleMember;
import com.planus.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailResponseDto {
    private Long id;
    private String nickname;
    private String email;

    private List<SimpleScheduleResponseDto> schedules;

    public static UserDetailResponseDto from(User user) {
        List<Schedule> schedules = user.getScheduleMembers().stream()
                .map(ScheduleMember::getSchedule)
                .collect(Collectors.toList());

        return UserDetailResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .schedules(schedules.stream()
                        .map(SimpleScheduleResponseDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
