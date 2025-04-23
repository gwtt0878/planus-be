package com.planus.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScheduleCreateRequestDto {
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "설명은 필수 입력 항목입니다.")
    private String description;

    @Future(message = "미팅 시간은 현재 시간 이후로 설정해야 합니다.")
    @NotNull(message = "미팅 시간은 필수 입력 항목입니다.")
    private LocalDateTime meetingDateTime;

    @NotBlank(message = "미팅 장소는 필수 입력 항목입니다.")
    private String meetingPlace;
}
