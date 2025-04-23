package com.planus.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.planus.dto.ScheduleListResponseDto;
import com.planus.dto.ScheduleResponseDto;
import com.planus.service.ScheduleService;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ScheduleService scheduleService;

        @Test
        public void testGetSchedules() throws Exception {
                ScheduleResponseDto scheduleResponseDto = ScheduleResponseDto.builder()
                                .id(1L)
                                .title("testSchedule")
                                .description("testDescription")
                                .meetingDateTime(LocalDateTime.now().plusDays(1))
                                .meetingPlace("testPlace")
                                .creatorNickname("testUser")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                ScheduleListResponseDto scheduleListResponseDto = ScheduleListResponseDto.builder()
                                .schedules(Arrays.asList(scheduleResponseDto))
                                .build();

                when(scheduleService.getSchedules()).thenReturn(scheduleListResponseDto);

                mockMvc.perform(get("/schedule"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("schedules").isArray())
                                .andExpect(jsonPath("schedules").isNotEmpty())
                                .andExpect(jsonPath("schedules[0].id").value(1L))
                                .andExpect(jsonPath("schedules[0].creatorNickname").value("testUser"))
                                .andExpect(jsonPath("schedules[0].meetingPlace").value("testPlace"))
                                .andExpect(jsonPath("schedules[0].title").value("testSchedule"))
                                .andExpect(jsonPath("schedules[0].description").value("testDescription"));
        }
}
