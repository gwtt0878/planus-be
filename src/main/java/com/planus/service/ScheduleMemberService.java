package com.planus.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.planus.entity.Schedule;
import com.planus.entity.ScheduleMember;
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

        public List<User> getMembers(Long scheduleId) {
                Schedule schedule = scheduleRepository.findById(scheduleId)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));
                return schedule.getScheduleMembers().stream().map(ScheduleMember::getUser).collect(Collectors.toList());
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

                Set<Long> existingMemberIds = existingMembers.stream().map(ScheduleMember::getUser).map(User::getId)
                                .collect(Collectors.toSet());
                Set<Long> newMemberIds = members.stream().map(User::getId).collect(Collectors.toSet());

                List<ScheduleMember> membersToAdd = members.stream()
                                .filter(member -> !existingMemberIds.contains(member.getId()))
                                .map(member -> ScheduleMember.builder().schedule(schedule).user(member).build())
                                .collect(Collectors.toList());

                List<ScheduleMember> membersToRemove = existingMembers.stream()
                                .filter(member -> !newMemberIds.contains(member.getUser().getId()))
                                .collect(Collectors.toList());

                scheduleMemberRepository.saveAll(membersToAdd);
                scheduleMemberRepository.deleteAll(membersToRemove);
        }
}