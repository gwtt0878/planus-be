package com.planus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planus.dto.ScheduleCreateRequestDto;
import com.planus.dto.ScheduleUpdateRequestDto;
import com.planus.entity.Schedule;
import com.planus.service.ScheduleService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final HttpSession httpSession;

    public ScheduleController(ScheduleService scheduleService, HttpSession httpSession) {
        this.scheduleService = scheduleService;
        this.httpSession = httpSession;
    }

    @PostMapping("/")
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleCreateRequestDto requestDto) {
        Long userId = (Long) httpSession.getAttribute("userId");
        Schedule schedule = scheduleService.createSchedule(requestDto, userId);
        return ResponseEntity.ok().body(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id,
            @RequestBody ScheduleUpdateRequestDto requestDto) {
        Long userId = (Long) httpSession.getAttribute("userId");
        Schedule schedule = scheduleService.updateSchedule(id, requestDto, userId);
        return ResponseEntity.ok().body(schedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        Long userId = (Long) httpSession.getAttribute("userId");
        scheduleService.deleteSchedule(id, userId);
        return ResponseEntity.ok().body("일정 삭제 성공");
    }
}
