package com.planus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planus.auth.UserContext;
import com.planus.common.annotations.LoginRequired;
import com.planus.dto.ScheduleCreateRequestDto;
import com.planus.dto.ScheduleListResponseDto;
import com.planus.dto.ScheduleModifiedResponseDto;
import com.planus.dto.ScheduleResponseDto;
import com.planus.dto.ScheduleUpdateRequestDto;
import com.planus.entity.ScheduleMemberStatus;
import com.planus.service.ScheduleMemberService;
import com.planus.service.ScheduleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleMemberService scheduleMemberService;

    @GetMapping()
    public ResponseEntity<ScheduleListResponseDto> getSchedules() {
        ScheduleListResponseDto scheduleListResponseDto = scheduleService.getSchedules();
        return ResponseEntity.ok().body(scheduleListResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.getSchedule(id);
        return ResponseEntity.ok().body(scheduleResponseDto);
    }

    @PostMapping()
    @LoginRequired
    public ResponseEntity<ScheduleModifiedResponseDto> createSchedule(
            @Valid @RequestBody ScheduleCreateRequestDto requestDto) {
        Long userId = UserContext.getUserId();
        scheduleService.createSchedule(requestDto, userId);

        ScheduleModifiedResponseDto scheduleModifiedResponseDto = ScheduleModifiedResponseDto.builder()
                .message("일정 생성 성공").build();

        return ResponseEntity.ok().body(scheduleModifiedResponseDto);
    }

    @PutMapping("/{id}")
    @LoginRequired
    public ResponseEntity<ScheduleModifiedResponseDto> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateRequestDto requestDto) {
        Long userId = UserContext.getUserId();
        scheduleService.updateSchedule(id, requestDto, userId);

        ScheduleModifiedResponseDto scheduleModifiedResponseDto = ScheduleModifiedResponseDto.builder()
                .message("일정 수정 성공").build();

        return ResponseEntity.ok().body(scheduleModifiedResponseDto);
    }

    @DeleteMapping("/{id}")
    @LoginRequired
    public ResponseEntity<ScheduleModifiedResponseDto> deleteSchedule(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        scheduleService.deleteSchedule(id, userId);

        ScheduleModifiedResponseDto scheduleModifiedResponseDto = ScheduleModifiedResponseDto.builder()
                .message("일정 삭제 성공").build();

        return ResponseEntity.ok().body(scheduleModifiedResponseDto);
    }

    @PatchMapping("/{scheduleId}/members/me")
    @LoginRequired
    public ResponseEntity<ScheduleModifiedResponseDto> updateStatus(
            @PathVariable Long scheduleId,
            @RequestParam String status) {

        Long userId = UserContext.getUserId();
        scheduleMemberService.updateStatus(scheduleId, userId, ScheduleMemberStatus.valueOf(status));

        ScheduleModifiedResponseDto scheduleModifiedResponseDto = ScheduleModifiedResponseDto.builder()
                .message("일정 참가자 상태 갱신 성공").build();

        return ResponseEntity.ok().body(scheduleModifiedResponseDto);
    }
}
