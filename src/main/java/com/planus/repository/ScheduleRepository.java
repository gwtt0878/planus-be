package com.planus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planus.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
