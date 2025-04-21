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
import com.planus.dto.ScheduleResponseDto;
import com.planus.dto.ScheduleUpdateRequestDto;
import com.planus.entity.Schedule;
import com.planus.service.ScheduleService;

import jakarta.servlet.http.HttpSession;

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
    public ResponseEntity<Schedule> createSchedule(
            @RequestBody ScheduleCreateRequestDto requestDto,
            HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        Schedule schedule = scheduleService.createSchedule(requestDto, userId);
        return ResponseEntity.ok().body(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateRequestDto requestDto,
            HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        Schedule schedule = scheduleService.updateSchedule(id, requestDto, userId);
        return ResponseEntity.ok().body(schedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        scheduleService.deleteSchedule(id, userId);
        return ResponseEntity.ok().body("일정 삭제 성공");
    }
}
