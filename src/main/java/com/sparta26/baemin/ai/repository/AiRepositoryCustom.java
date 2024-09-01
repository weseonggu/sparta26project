package com.sparta26.baemin.ai.repository;

import com.sparta26.baemin.dto.ai.RequestSearchAiDto;
import com.sparta26.baemin.dto.ai.ResponseSearchAiDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AiRepositoryCustom {
    Page<ResponseSearchAiDto> findAllAi(Pageable pageable, RequestSearchAiDto condition);
}
