package com.sparta26.baemin.store.controller;

import com.sparta26.baemin.dto.store.RequestStoreDto;
import com.sparta26.baemin.dto.store.ResponseFindStoreDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.ForbiddenAccessException;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.jwt.ForContext;
import com.sparta26.baemin.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class StoreController {

    private final StoreService storeService;

    /**
     * 가게 생성, 가게주인만 생성가능
     *
     * @param requestStoreDto
     * @return
     */
    @PostMapping("/store")
    public ResponseEntity<?> createStore(@Valid @RequestBody RequestStoreDto requestStoreDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("가게 생성 시도 중 | requestStoreDto = {} | Member = {}", requestStoreDto, customUserDetails.getForContext());
        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String role = context.getRole();
        String email = context.getEmail();


        if (!role.equals("ROLE_OWNER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        ResponseStoreDto responseStoreDto = storeService.createStore(requestStoreDto, memberId, email);
        log.info("가게 생성 완료 | responseStoreDto = {} | Member = {}", responseStoreDto, memberId);
        return ResponseEntity.ok(responseStoreDto);
    }

    /**
     * 가게 단건 조회
     *
     * @param storeId
     * @return
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> findOneStore(@PathVariable("storeId") String storeId, Pageable pageable) {
        log.info("가게 단건 조회 시도 중 | storeId = {}", storeId);

        Page<ResponseFindStoreDto> responseFindStoreDto = storeService.findOneStore(storeId, pageable);

        log.info("가게 단건 조회 완료 | responseStoreDto = {}", responseFindStoreDto);
        return ResponseEntity.ok(responseFindStoreDto);
    }

    /**
     * 가게 전체 조회
     */
    @GetMapping("/store")
    public ResponseEntity<?> findAllStore(Pageable pageable) {
        log.info("가게 전체 조회 시도 중");

        Page<ResponseFindStoreDto> responseFindStoreDto = storeService.findAllStore(pageable);
        return ResponseEntity.ok(responseFindStoreDto);
    }

    /**
     * 가게 수정, 가게주인 관리자 가능
     *
     * @param requestStoreDto
     * @param customUserDetails
     * @param storeId
     * @return
     */
    @PatchMapping("/store/{storeId}")
    public ResponseEntity<?> updateStore(@Valid @RequestBody RequestStoreDto requestStoreDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @PathVariable("storeId") String storeId) {
        log.info("가게 수정 시도 중 | storeId = {}, requestStoreDto = {}", storeId, requestStoreDto);

        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String role = context.getRole();
        String email = context.getEmail();

        if (!role.equals("ROLE_OWNER") && !role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        ResponseStoreDto responseStoreDto = storeService.updateStore(requestStoreDto, storeId, memberId, email, role);

        return ResponseEntity.ok(responseStoreDto);
    }

    /**
     * 가게 활성화 메서드, 가게 주인
     *
     * @param customUserDetails
     * @return
     */
    @PatchMapping("/store/active")
    public ResponseEntity<?> activeStore(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("가게 활성화 시도 중 | memberId = {}", customUserDetails.getForContext().getId());
        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();

        String active = storeService.activeStore(memberId);
        log.info("가게 활성화 완료");
        return ResponseEntity.ok(active);
    }

    /**
     * 가게 비활성화 메서드, 가게 주인
     *
     * @param customUserDetails
     * @return
     */
    @PatchMapping("/store/inactive")
    public ResponseEntity<?> inactiveStore(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("가게 비활성화 시도 중 | memberId = {}", customUserDetails.getForContext().getId());
        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();

        String inactive = storeService.inactiveStore(memberId);
        log.info("가게 비활성화 완료");
        return ResponseEntity.ok(inactive);
    }

    /**
     * 가게 삭제, 가게 주인 관리자 가능
     * @param customUserDetails
     * @param storeId
     * @return
     */
    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<?> deleteStore(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @PathVariable("storeId") String storeId) {
        log.info("가게 삭제 시도 중 | storeId = {}", storeId);
        ForContext context = customUserDetails.getForContext();
        String email = context.getEmail();
        String role = context.getRole();
        Long memberId = context.getId();

        if (!role.equals("ROLE_OWNER") && !role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        storeService.deleteStore(storeId, email, role, memberId);
        log.info("가게 삭제 완료");
        return ResponseEntity.ok("가게 삭제 완료");
    }
}
