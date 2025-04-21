package com.planus.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.planus.entity.Schedule;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleListResponseDto {
    private List<ScheduleResponseDto> schedules;

    public static ScheduleListResponseDto of(List<Schedule> schedules) {
        List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());

        return ScheduleListResponseDto.builder()
                .schedules(scheduleResponseDtos)
                .build();
    }
}
