package com.planus.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planus.common.exception.GlobalExceptionHandler;
import com.planus.dto.ScheduleCreateRequestDto;
import com.planus.dto.ScheduleListResponseDto;
import com.planus.dto.ScheduleListResponseDto.SimpleScheduleResponseDto;
import com.planus.dto.ScheduleUpdateRequestDto;
import com.planus.service.ScheduleService;

@WebMvcTest(ScheduleController.class)
@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ScheduleService scheduleService;

        @Autowired
        private ObjectMapper objectMapper;

        protected MockHttpSession session;

        @BeforeEach
        public void setUp() {
                session = new MockHttpSession();
                session.setAttribute("userId", 1L);
        }

        @Test
        @DisplayName("일정 조회 테스트")
        public void testGetSchedules() throws Exception {
                SimpleScheduleResponseDto simpleScheduleResponseDto = SimpleScheduleResponseDto.builder()
                                .id(1L)
                                .title("testSchedule")
                                .description("testDescription")
                                .meetingDateTime(LocalDateTime.now().plusDays(1))
                                .meetingPlace("testPlace")
                                .creatorNickname("testUser")
                                .build();

                ScheduleListResponseDto scheduleListResponseDto = ScheduleListResponseDto.builder()
                                .schedules(Arrays.asList(simpleScheduleResponseDto))
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

        @Test
        @DisplayName("일정 생성 테스트")
        public void testCreateSchedule() throws Exception {
                ScheduleCreateRequestDto scheduleCreateRequestDto = ScheduleCreateRequestDto.builder()
                                .title("testSchedule")
                                .description("testDescription")
                                .meetingDateTime(LocalDateTime.now().plusDays(1))
                                .meetingPlace("testPlace")
                                .build();

                doNothing().when(scheduleService).createSchedule(any(ScheduleCreateRequestDto.class), eq(1L));

                mockMvc.perform(post("/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleCreateRequestDto))
                                .session(session))
                                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("일정 수정 테스트")
        public void testUpdateSchedule() throws Exception {
                ScheduleUpdateRequestDto scheduleUpdateRequestDto = ScheduleUpdateRequestDto.builder()
                                .title("updatedSchedule")
                                .description("updatedDescription")
                                .meetingDateTime(LocalDateTime.now().plusDays(2))
                                .meetingPlace("updatedPlace")
                                .build();

                doNothing().when(scheduleService).updateSchedule(eq(1L), any(ScheduleUpdateRequestDto.class), eq(1L));

                mockMvc.perform(put("/schedule/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleUpdateRequestDto))
                                .session(session))
                                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("일정 수정 테스트 실패 - 다른 유저가 시도할 때")
        public void testUpdateScheduleWithInvalidUser() throws Exception {
                session.setAttribute("userId", 2L);
                ScheduleUpdateRequestDto scheduleUpdateRequestDto = ScheduleUpdateRequestDto.builder()
                                .title("updatedSchedule")
                                .description("updatedDescription")
                                .meetingDateTime(LocalDateTime.now().plusDays(2))
                                .meetingPlace("updatedPlace")
                                .build();

                doThrow(new IllegalArgumentException("일정을 수정할 권한이 없습니다."))
                                .when(scheduleService)
                                .updateSchedule(eq(1L), any(ScheduleUpdateRequestDto.class), eq(2L));

                mockMvc.perform(put("/schedule/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleUpdateRequestDto))
                                .session(session))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("일정 삭제 테스트 실패 - 다른 유저가 시도할 때")
        public void testDeleteScheduleWithInvalidUser() throws Exception {
                session.setAttribute("userId", 2L);
                doThrow(new IllegalArgumentException("일정을 삭제할 권한이 없습니다."))
                                .when(scheduleService)
                                .deleteSchedule(1L, 2L);

                mockMvc.perform(delete("/schedule/1")
                                .session(session))
                                .andExpect(status().isBadRequest());

        }

        @Test
        @DisplayName("일정 삭제 테스트")
        public void testDeleteSchedule() throws Exception {
                doNothing().when(scheduleService).deleteSchedule(1L, 1L);

                mockMvc.perform(delete("/schedule/1")
                                .session(session))
                                .andExpect(status().isOk());

        }

        @Test
        @DisplayName("일정 삭제 테스트 실패 - 존재하지 않는 일정")
        public void testDeleteScheduleWithInvalidId() throws Exception {
                doThrow(new NoSuchElementException("일정을 찾을 수 없습니다."))
                                .when(scheduleService)
                                .deleteSchedule(999L, 1L);

                mockMvc.perform(delete("/schedule/999")
                                .session(session))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("입력 DTO 검증 - 과거 일정")
        public void testCreateScheduleWithPreviousDate() throws Exception {
                ScheduleCreateRequestDto scheduleCreateRequestDto = ScheduleCreateRequestDto.builder()
                                .title("testSchedule")
                                .description("testDescription")
                                .meetingDateTime(LocalDateTime.now().minusDays(1))
                                .meetingPlace("testPlace")
                                .build();

                mockMvc.perform(post("/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scheduleCreateRequestDto))
                                .session(session))
                                .andExpect(status().isBadRequest());
        }
}
