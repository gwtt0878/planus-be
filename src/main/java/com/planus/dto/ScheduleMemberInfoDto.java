package com.planus.dto;

import com.planus.entity.ScheduleMember;
import com.planus.entity.ScheduleMemberStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleMemberInfoDto {
    private Long memberId;
    private String nickname;
    private ScheduleMemberStatus status;

    public static ScheduleMemberInfoDto from(ScheduleMember scheduleMember) {
        return ScheduleMemberInfoDto.builder()
                .memberId(scheduleMember.getUser().getId())
                .nickname(scheduleMember.getUser().getNickname())
                .status(scheduleMember.getStatus())
                .build();
    }
}
