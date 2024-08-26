package com.sparta26.baemin.ai.repository;

import com.sparta26.baemin.ai.entity.Ai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiRepository extends JpaRepository<Ai, UUID> {
}
