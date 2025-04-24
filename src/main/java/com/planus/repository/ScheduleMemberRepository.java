package com.planus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planus.entity.ScheduleMember;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {

}
