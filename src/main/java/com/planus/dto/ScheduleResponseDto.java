package com.planus.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.planus.entity.Schedule;
import com.planus.entity.ScheduleMember;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime meetingDateTime;
    private String meetingPlace;
    private String creatorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ScheduleMemberInfoDto> members;

    public static ScheduleResponseDto from(Schedule schedule, List<ScheduleMember> members) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .meetingDateTime(schedule.getMeetingDateTime())
                .meetingPlace(schedule.getMeetingPlace())
                .creatorNickname(schedule.getCreator().getNickname())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .members(members.stream().map(ScheduleMemberInfoDto::from).collect(Collectors.toList()))
                .build();
    }
}
