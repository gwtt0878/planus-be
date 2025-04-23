package com.planus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planus.dto.ScheduleCreateRequestDto;
import com.planus.dto.ScheduleListResponseDto;
import com.planus.dto.ScheduleModifiedResponseDto;
import com.planus.dto.ScheduleResponseDto;
import com.planus.dto.ScheduleUpdateRequestDto;
import com.planus.service.ScheduleService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

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
    public ResponseEntity<ScheduleModifiedResponseDto> createSchedule(
            @Valid @RequestBody ScheduleCreateRequestDto requestDto,
            HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        scheduleService.createSchedule(requestDto, userId);

        ScheduleModifiedResponseDto scheduleModifiedResponseDto = ScheduleModifiedResponseDto.builder()
                .message("일정 생성 성공").build();

        return ResponseEntity.ok().body(scheduleModifiedResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleModifiedResponseDto> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateRequestDto requestDto,
            HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        scheduleService.updateSchedule(id, requestDto, userId);

        ScheduleModifiedResponseDto scheduleModifiedResponseDto = ScheduleModifiedResponseDto.builder()
                .message("일정 수정 성공").build();

        return ResponseEntity.ok().body(scheduleModifiedResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ScheduleModifiedResponseDto> deleteSchedule(@PathVariable Long id, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        scheduleService.deleteSchedule(id, userId);

        ScheduleModifiedResponseDto scheduleModifiedResponseDto = ScheduleModifiedResponseDto.builder()
                .message("일정 삭제 성공").build();

        return ResponseEntity.ok().body(scheduleModifiedResponseDto);
    }
}
