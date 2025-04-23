package com.planus.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.planus.dto.ScheduleCreateRequestDto;
import com.planus.entity.Schedule;
import com.planus.entity.User;
import com.planus.repository.ScheduleRepository;
import com.planus.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    public void testCreateSchedule() {
        User testUser = User.builder()
                .nickname("testUser")
                .email("test@test.com")
                .password("testPassword")
                .build();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        ScheduleCreateRequestDto requestDto = ScheduleCreateRequestDto.builder()
                .title("testSchedule")
                .description("testDescription")
                .meetingDateTime(LocalDateTime.now().plusDays(1))
                .meetingPlace("testPlace")
                .build();

        Schedule schedule = Schedule.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .meetingDateTime(requestDto.getMeetingDateTime())
                .meetingPlace(requestDto.getMeetingPlace())
                .creator(testUser)
                .build();

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule savedSchedule = scheduleService.createSchedule(requestDto, testUser.getId());

        assertThat(savedSchedule).isEqualTo(schedule);
    }
}
