package com.sparta26.baemin.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta26.baemin.ai.service.AiService;
import com.sparta26.baemin.dto.ai.RequestAiDto;
import com.sparta26.baemin.dto.ai.ResponseAiDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/v1/questions")
    public ResponseEntity<?> questions(@Valid @RequestBody RequestAiDto requestAiDto) throws JsonProcessingException {

        ResponseAiDto responseAiDto = aiService.questions(requestAiDto);
        return ResponseEntity.ok(responseAiDto);
    }
}
