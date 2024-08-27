package com.sparta26.baemin.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta26.baemin.ai.service.AiService;
import com.sparta26.baemin.dto.ai.RequestAiDto;
import com.sparta26.baemin.dto.ai.ResponseAiAnswerDto;
import com.sparta26.baemin.dto.ai.ResponseAiPageDto;
import com.sparta26.baemin.exception.exceptionsdefined.ForbiddenAccessException;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.jwt.ForContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AiController {

    private final AiService aiService;

    /**
     * Ai 질문 메서드
     */
    @PostMapping("/questions")
    public ResponseEntity<?> questions(@Valid @RequestBody RequestAiDto requestAiDto,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) throws JsonProcessingException {
        log.info("Ai 질문 시도 중 : {}, 권한 : {}", requestAiDto, customUserDetails.getForContext().getRole());
        ForContext context = customUserDetails.getForContext();
        String role = context.getRole();
        String email = context.getEmail();
        if (!role.equals("ROLE_OWNER") && !role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        ResponseAiAnswerDto responseAiAnswerDto = aiService.questions(requestAiDto, email);

        log.info("Ai 질문 완료 : {}, 권한 : {}", requestAiDto, customUserDetails.getForContext().getRole());
        return ResponseEntity.ok(responseAiAnswerDto);
    }

    /**
     * Ai 질답 리스트 검색
     */
    @GetMapping("/questions")
    public ResponseEntity<?> questionList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          Pageable pageable) {
        log.info("Ai 질문 리스트 검색 시도 중 : {}", customUserDetails.getForContext());
        ForContext context = customUserDetails.getForContext();
        String role = context.getRole();

        if (!role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        Page<ResponseAiPageDto> responseAiPageDtos = aiService.findAll(pageable);
        log.info("Ai 질문 리스트 검색 완료");
        return ResponseEntity.ok(responseAiPageDtos);
    }

    /**
     * Ai 질답 삭제
     */
    @DeleteMapping("/questions/{aiId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable("aiId") String aiId) {
        log.info("Ai 질답 삭제 시도 중 | ai_id = {}", aiId);

        ForContext context = customUserDetails.getForContext();
        String role = context.getRole();
        String email = context.getEmail();

        if (!role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        aiService.delete(aiId, email);
        log.info("Ai 질답 삭제 완료");
        return ResponseEntity.ok("삭제 완료");
    }
}
