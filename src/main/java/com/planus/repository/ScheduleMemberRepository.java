package com.planus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planus.entity.ScheduleMember;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    List<ScheduleMember> findAllByScheduleId(Long scheduleId);

    Optional<ScheduleMember> findByScheduleIdAndUserId(Long scheduleId, Long userId);

    List<ScheduleMember> findAllByScheduleIdAndUserIdIn(Long scheduleId, List<Long> userIds);
}
