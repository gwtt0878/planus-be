package com.planus.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleMemberUpdateRequestDto {
    @NotEmpty(message = "멤버 ID 리스트는 필수 입력 항목입니다.")
    private List<Long> memberIds;
}
