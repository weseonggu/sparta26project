package com.sparta26.baemin.store.controller;

import com.sparta26.baemin.dto.store.RequestStoreDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.ForbiddenAccessException;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.jwt.ForContext;
import com.sparta26.baemin.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class StoreController {

    private final StoreService storeService;

    // 가게 생성
    @PostMapping("/store")
    public ResponseEntity<?> createStore(@Valid @RequestBody RequestStoreDto requestStoreDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails ) {
        log.info("가게 생성 시도 중 | requestStoreDto = {} | Member = {}", requestStoreDto, customUserDetails.getForContext());
        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String role = context.getRole();

        if (!role.equals("ROLE_OWNER") && !role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        ResponseStoreDto responseStoreDto = storeService.createStore(requestStoreDto,memberId);
        log.info("가게 생성 완료 | responseStoreDto = {} | Member = {}", responseStoreDto, memberId);
        return ResponseEntity.ok(responseStoreDto);
    }

    // 가게 단건 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> findOneStore(@PathVariable("storeId") String storeId) {

        ResponseStoreDto responseStoreDto = storeService.findOneStore(storeId);
        return ResponseEntity.ok(responseStoreDto);
    }

}
