package com.planus.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleUpdateRequestDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime meetingDateTime;
    private String meetingPlace;
}
