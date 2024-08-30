package com.sparta26.baemin.operatinghours.controller;

import com.sparta26.baemin.dto.operatinghours.RequestOperatingHoursDto;
import com.sparta26.baemin.dto.operatinghours.ResponseOperatingDto;
import com.sparta26.baemin.exception.exceptionsdefined.ForbiddenAccessException;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.jwt.ForContext;
import com.sparta26.baemin.operatinghours.service.OperatingHoursService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class OperatingHoursController {

    private final OperatingHoursService operatingHoursService;

    /**
     * 가게 운영시간 등록, 가게주인만 생성가능
     * @param requestOperatingHoursDto
     * @param customUserDetails
     * @param storeId
     * @return
     */
    @PostMapping("/operating/{storeId}")
    public ResponseEntity<?> createOperatingHours(@Valid @RequestBody RequestOperatingHoursDto requestOperatingHoursDto,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @PathVariable("storeId") String storeId) {

        log.info("가게 운영시간 등록 시도 중 | storeId = {},  memberId = {}", storeId, customUserDetails.getForContext().getId());
        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String email = context.getEmail();

        ResponseOperatingDto responseOperatingDto = operatingHoursService.createOperatingHours(requestOperatingHoursDto, storeId, memberId, email);

        log.info("가게 운영시간 등록 완료");
        return ResponseEntity.ok(responseOperatingDto);
    }

    /**
     * 가게 운영시간 수정, 가게주인 관리자 가능
     * @param requestOperatingHoursDto
     * @param customUserDetails
     * @param operatingId
     * @return
     */
    @PatchMapping("/operating/{operatingId}")
    public ResponseEntity<?> updateOperatingHours(@Valid @RequestBody RequestOperatingHoursDto requestOperatingHoursDto,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @PathVariable("operatingId") String operatingId) {
        log.info("가게 운영시간 수정 시도 중 | operatingId = {}, memberId = {}", operatingId, customUserDetails.getForContext().getId());
        ForContext context = customUserDetails.getForContext();
        String role = context.getRole();
        Long memberId = context.getId();
        String email = context.getEmail();

        if (!role.equals("ROLE_OWNER") && !role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        ResponseOperatingDto updatedOperatingHoursDto = operatingHoursService.updateOperatingHours(requestOperatingHoursDto, operatingId, memberId, email, role);
        log.info("가게 운영시간 수정 완료");
        return ResponseEntity.ok(updatedOperatingHoursDto);
    }

    /**
     * 가게 운영시간 삭제, 가게주인 관리자 가능
     * @param customUserDetails
     * @param operatingId
     * @return
     */
    @DeleteMapping("/operating/{operatingId}")
    public ResponseEntity<?> deleteOperatingHours(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @PathVariable("operatingId") String operatingId) {
        log.info("가게 운영시간 삭제 시도 중 | operatingId = {}, memberId = {}", operatingId, customUserDetails.getForContext().getId());
        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String email = context.getEmail();
        String role = context.getRole();

        if (!role.equals("ROLE_OWNER") && !role.equals("ROLE_MASTER")) {
            log.error("사용자 권한 불일치 = {}", context.getRole());
            throw new ForbiddenAccessException("Unauthorized user");
        }

        operatingHoursService.deleteOperatingHours(operatingId, email, role);
        return ResponseEntity.ok("삭제 완료");
    }

    /**
     * 가게 운영시간 단건 조회
     * @param storeId
     * @return
     */
    @GetMapping("/operating/{storeId}")
    public ResponseEntity<?> findOneOperatingHours(@PathVariable("storeId") String storeId) {
        log.info("가게 운영시간 조회 시도 중 | storeId = {}", storeId);

        List<ResponseOperatingDto> operatingHoursDtoList = operatingHoursService.findOneOperatingHours(storeId);
        log.info("가게 운영시간 조회 완료");
        return ResponseEntity.ok(operatingHoursDtoList);
    }


}
