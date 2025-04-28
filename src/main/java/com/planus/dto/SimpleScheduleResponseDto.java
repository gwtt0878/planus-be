package com.planus.dto;

import java.time.LocalDateTime;

import com.planus.entity.Schedule;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleScheduleResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime meetingDateTime;
    private String meetingPlace;
    private String creatorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SimpleScheduleResponseDto from(Schedule schedule) {
        return SimpleScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .meetingDateTime(schedule.getMeetingDateTime())
                .meetingPlace(schedule.getMeetingPlace())
                .creatorNickname(schedule.getCreator().getNickname())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}