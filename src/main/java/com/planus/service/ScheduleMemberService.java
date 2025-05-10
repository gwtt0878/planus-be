package com.planus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.planus.entity.Schedule;
import com.planus.entity.ScheduleMember;
import com.planus.entity.ScheduleMemberStatus;
import com.planus.entity.User;
import com.planus.repository.ScheduleMemberRepository;
import com.planus.repository.ScheduleRepository;
import com.planus.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleMemberService {
        private final ScheduleMemberRepository scheduleMemberRepository;
        private final ScheduleRepository scheduleRepository;
        private final UserRepository userRepository;

        public List<ScheduleMember> getMembers(Long scheduleId) {
                return scheduleMemberRepository.findAllByScheduleId(scheduleId);
        }

        @Transactional
        public void updateMembers(Long scheduleId, List<Long> memberIds) {
                Schedule schedule = scheduleRepository.findById(scheduleId)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));
                List<User> members = userRepository.findAllById(memberIds);

                if (members.size() != memberIds.size()) {
                        List<Long> notFoundMemberIds = memberIds.stream()
                                        .filter(id -> !members.stream().anyMatch(member -> member.getId().equals(id)))
                                        .collect(Collectors.toList());

                        String notFoundMemberIdsString = notFoundMemberIds.stream()
                                        .map(String::valueOf)
                                        .collect(Collectors.joining(", "));

                        throw new IllegalArgumentException("존재하지 않는 유저가 있습니다. : " + notFoundMemberIdsString);
                }

                List<ScheduleMember> existingMembers = scheduleMemberRepository.findAllByScheduleId(scheduleId);

                Set<Long> existingMemberIds = extractUserIds(existingMembers);
                Set<Long> newMemberIds = extractUserIds(members);

                List<Long> memberIdsToRemove = new ArrayList<>(existingMemberIds);
                memberIdsToRemove.removeAll(newMemberIds);
                newMemberIds.removeAll(existingMemberIds);

                List<User> newMembers = userRepository.findAllById(newMemberIds);

                List<ScheduleMember> membersToAdd = newMembers.stream()
                                .map(member -> ScheduleMember.builder().schedule(schedule).user(member)
                                                .status(ScheduleMemberStatus.PENDING).build())
                                .collect(Collectors.toList());

                List<ScheduleMember> membersToRemove = scheduleMemberRepository.findAllByScheduleIdAndUserIdIn(
                                scheduleId,
                                memberIdsToRemove);

                scheduleMemberRepository.saveAll(membersToAdd);
                scheduleMemberRepository.deleteAll(membersToRemove);
        }

        public void updateStatus(Long scheduleId, Long userId, ScheduleMemberStatus status) {
                ScheduleMember scheduleMember = scheduleMemberRepository.findByScheduleIdAndUserId(scheduleId, userId)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정 멤버입니다."));
                scheduleMember.updateStatus(status);
        }

        private Set<Long> extractUserIds(List<? extends Object> members) {
                if (members.isEmpty()) {
                        return Set.of();
                }
                if (members.get(0) instanceof ScheduleMember) {
                        return members.stream()
                                        .map(m -> ((ScheduleMember) m).getUser().getId())
                                        .collect(Collectors.toSet());
                }
                if (members.get(0) instanceof User) {
                        return members.stream()
                                        .map(u -> ((User) u).getId())
                                        .collect(Collectors.toSet());
                }
                throw new IllegalArgumentException("지원하지 않는 타입입니다.");
        }
}