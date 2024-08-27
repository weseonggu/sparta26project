package com.sparta26.baemin.ai.repository;

import com.sparta26.baemin.ai.entity.Ai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AiRepository extends JpaRepository<Ai, UUID> {

    @Query("select a from Ai a where a.isPublic = true")
    Page<Ai> findAllByIsPublic(Pageable pageable);
}
