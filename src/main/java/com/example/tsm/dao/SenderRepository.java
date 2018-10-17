package com.example.tsm.dao;

import com.example.tsm.entity.SenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<SenderEntity, String> {
}
