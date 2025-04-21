package com.planus.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planus.dto.ScheduleCreateRequestDto;
import com.planus.dto.ScheduleListResponseDto;
import com.planus.dto.ScheduleResponseDto;
import com.planus.dto.ScheduleUpdateRequestDto;
import com.planus.entity.Schedule;
import com.planus.entity.User;
import com.planus.repository.ScheduleRepository;
import com.planus.repository.UserRepository;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    public ScheduleListResponseDto getSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return ScheduleListResponseDto.of(schedules);
    }

    public ScheduleResponseDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));
        return ScheduleResponseDto.from(schedule);
    }

    public Schedule createSchedule(ScheduleCreateRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Schedule schedule = Schedule.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .meetingDateTime(requestDto.getMeetingDateTime())
                .meetingPlace(requestDto.getMeetingPlace())
                .creator(user)
                .build();

        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, ScheduleUpdateRequestDto requestDto, Long userId) {
        Schedule schedule = scheduleRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!schedule.getCreator().getId().equals(userId)) {
            throw new IllegalArgumentException("일정을 수정할 권한이 없습니다.");
        }

        schedule.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getMeetingDateTime(),
                requestDto.getMeetingPlace());

        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id, Long userId) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!schedule.getCreator().getId().equals(userId)) {
            throw new IllegalArgumentException("일정을 삭제할 권한이 없습니다.");
        }
        scheduleRepository.deleteById(id);
    }
}