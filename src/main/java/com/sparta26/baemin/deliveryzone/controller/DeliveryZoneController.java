package com.sparta26.baemin.deliveryzone.controller;

import com.sparta26.baemin.deliveryzone.service.DeliveryZoneService;
import com.sparta26.baemin.dto.deliveryzone.RequestDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.RequestSearchDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseSearchDeliveryDto;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.jwt.ForContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1")
public class DeliveryZoneController {

    private final DeliveryZoneService deliveryZoneService;

    /**
     * 배달 지역 생성 | 가게 주인
     *
     * @param storeId
     * @param request
     * @param customUserDetails
     * @return
     */
    @PostMapping("/delivery/{storeId}")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_OWNER')")
    public ResponseEntity<?> createDeliveryZone(@PathVariable("storeId") UUID storeId,
                                                @Valid @RequestBody RequestDeliveryDto request,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("배달 지역 생성 시도 중 | storeId = {}, request = {}", storeId, request);

        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();

        ResponseDeliveryDto response = deliveryZoneService.createDeliveryZone(storeId, request, memberId);

        log.info("배달 지역 생성 완료 | storeId = {}, response = {}", storeId, response);
        return ResponseEntity.ok(response);
    }

    /**
     * 배달 지역 수정 | 가게 주인, 관리자
     *
     * @param deliveryId
     * @param request
     * @param customUserDetails
     * @return
     */
    @PatchMapping("/delivery/{deliveryId}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ROLE_MASTER', 'ROLE_OWNER')")
    public ResponseEntity<?> updateDeliveryZone(@PathVariable("deliveryId") UUID deliveryId,
                                                @RequestBody RequestDeliveryDto request,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("배달 지역 수정 시도 중 | deliveryId = {}, request = {}", deliveryId, request);

        ForContext context = customUserDetails.getForContext();
        String email = context.getEmail();
        String role = context.getRole();

        ResponseDeliveryDto response = deliveryZoneService.updateDeliveryZone(deliveryId, request, email, role);
        log.info("배달 지역 수정 완료 | deliveryId = {}, response = {}", deliveryId, response);
        return ResponseEntity.ok(response);
    }

    /**
     * 배달 지역 삭제 | 가게 주인, 관리자
     *
     * @param deliveryId
     * @param customUserDetails
     * @return
     */
    @DeleteMapping("/delivery/{deliveryId}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ROLE_MASTER', 'ROLE_OWNER')")
    public ResponseEntity<?> deleteDeliveryZone(@PathVariable("deliveryId") UUID deliveryId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("배달 지역 삭제 시도 중 | deliveryId = {}", deliveryId);

        ForContext context = customUserDetails.getForContext();
        String email = context.getEmail();
        String role = context.getRole();

        String result = deliveryZoneService.deleteDeliveryZone(deliveryId, email, role);
        log.info("배달 지역 삭제 완료 | deliveryId = {}", deliveryId);
        return ResponseEntity.ok(result);
    }

    /**
     * 배달 지역 전체 조회 | 관리자
     * @param condition
     * @param pageable
     * @return
     */
    @GetMapping("/delivery")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_MASTER')")
    public ResponseEntity<?> findAllDeliveryZone(RequestSearchDeliveryDto condition,
                                                 Pageable pageable) {
        log.info("배달 지역 전체 조회 시도 중 | request = {}", condition);

        Page<ResponseSearchDeliveryDto> response = deliveryZoneService.findAllDeliveryZone(condition, pageable);
        log.info("배달 지역 전제 조회 완료");
        return ResponseEntity.ok(response);
    }
}
