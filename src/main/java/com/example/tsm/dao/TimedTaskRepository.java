package com.example.tsm.dao;

import com.example.tsm.entity.TimedTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimedTaskRepository extends JpaRepository<TimedTaskEntity, String> {
}
