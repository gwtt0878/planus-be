package com.planus.dto;

import com.planus.entity.ScheduleMember;
import com.planus.entity.ScheduleMemberStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleMemberInfoDto {
    private Long id;
    private String nickname;
    private ScheduleMemberStatus status;

    public static ScheduleMemberInfoDto from(ScheduleMember scheduleMember) {
        return ScheduleMemberInfoDto.builder()
                .id(scheduleMember.getId())
                .nickname(scheduleMember.getUser().getNickname())
                .status(scheduleMember.getStatus())
                .build();
    }
}
