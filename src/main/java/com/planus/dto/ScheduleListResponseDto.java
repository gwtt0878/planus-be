package com.planus.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.planus.entity.Schedule;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleListResponseDto {
    private List<SimpleScheduleResponseDto> schedules;

    public static ScheduleListResponseDto of(List<Schedule> schedules) {
        List<SimpleScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                .map(SimpleScheduleResponseDto::from)
                .collect(Collectors.toList());

        return ScheduleListResponseDto.builder()
                .schedules(scheduleResponseDtos)
                .build();
    }
}
