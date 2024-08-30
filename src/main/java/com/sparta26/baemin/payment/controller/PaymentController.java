package com.sparta26.baemin.payment.controller;

import com.sparta26.baemin.dto.payment.RequestPaymentDto;
import com.sparta26.baemin.dto.payment.RequestPaymentUpdateDto;
import com.sparta26.baemin.dto.payment.ResponsePaymentInfoDto;
import com.sparta26.baemin.exception.exceptionsdefined.NotFoundException;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제
     *
     * @param request 결제에 필요한 데이터
     * @return {@link ResponsePaymentInfoDto} 객체
     */
    @PostMapping
    public ResponseEntity<ResponsePaymentInfoDto> pay(
            @RequestBody RequestPaymentDto request
    ) {

        return ResponseEntity.ok(
                paymentService.pay(request));
    }

    /**
     * 결제 조회
     * <p>'ROLE_CUSTOMER' - 본인 결제 내역, 'ROLE_OWNER' - 본인 가게에 해당하는 결제내역만 조회,
     * 'ROLE_MANAGER' 이상 관리자는 paymentId 로 조회 가능<p/>
     *
     * @param paymentId 조회하려는 결제 ID
     * @param customUserDetails 사용자 정보
     * @return {@link ResponsePaymentInfoDto} 객체
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponsePaymentInfoDto> getPayment(
            @PathVariable String paymentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        return ResponseEntity.ok(
                paymentService.getPayment(
                        paymentId, customUserDetails.getRole(), customUserDetails.getEmail())
        );
    }

    /**
     * 여러 결제 조회
     * <p>'ROLE_CUSTOMER' - 본인 결제 내역, 'ROLE_OWNER' - 본인 가게에 해당하는 결제내역만 조회,
     * 'ROLE_MANAGER' 이상 관리자는 모두 조회 가능<p/>
     *
     * @param startDate nullable 조회하려는 시작 날짜
     * @param endDate nullable 조회하려는 끝 날짜
     * @param pageable default_sort - createdAt, updatedAt DESC
     * @param customUserDetails 사용자 정보
     * @return {@link Page}<{@link ResponsePaymentInfoDto}> 페이징 처리된 객체
     */
    @GetMapping
    public ResponseEntity<Page<ResponsePaymentInfoDto>> getPayments(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
         return ResponseEntity.ok(paymentService.getPayments(
                 startDate,
                 endDate,
                 pageable,
                 customUserDetails.getEmail(),
                 customUserDetails.getRole()
         ));
    }

    /**
     * 결제 수정
     * <p>'ROLE_MASTER' 권한만 데이터 수정 가능<p/>
     *
     * @param paymentId 수정하려는 결제 ID
     * @param request 수정하려는 내용
     * @return {@link ResponsePaymentInfoDto} 객체
     */
    @PatchMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<ResponsePaymentInfoDto> updatePayment(
            @PathVariable String paymentId,
            @RequestBody RequestPaymentUpdateDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                paymentService.updatePayment(
                        paymentId, request.getStatus(), customUserDetails.getRole()
                ));
    }

    /**
     * 결제 삭제
     * <p>'ROLE_MASTER' 권한만 데이터 삭제(숨김처리)가능<p/>
     *
     * @param paymentId 삭제하려는 결제 ID
     * @param customUserDetails 사용자 정보
     * @return Boolean 타입의 결과 : TURE - 성공
     * @exception NotFoundException 결제정보를 찾지 못하면 예외 발생
     */
    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<Boolean> deletePayment(
            @PathVariable String paymentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                paymentService.deletePayment(
                        paymentId,
                        customUserDetails.getEmail(),
                        customUserDetails.getRole()
                ));
    }
}