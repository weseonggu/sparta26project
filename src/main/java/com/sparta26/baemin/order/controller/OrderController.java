package com.sparta26.baemin.order.controller;

import com.sparta26.baemin.dto.order.RequestOrderCreateDto;
import com.sparta26.baemin.dto.order.RequestOrderUpdateDto;
import com.sparta26.baemin.dto.order.ResponseOrderCreateDto;
import com.sparta26.baemin.dto.order.ResponseOrderDto;
import com.sparta26.baemin.exception.exceptionsdefined.NotFoundException;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     *
     * @param request 주문생성에 필요한 데이터
     * @param customUserDetails 사용자 정보
     * @return {@link ResponseOrderCreateDto} 객체
     */

    @PostMapping
    public ResponseEntity<ResponseOrderCreateDto> createOrder(
            @RequestBody RequestOrderCreateDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                orderService.createOrder(request, customUserDetails.getId()));
    }

    /**
     * 주문 조회
     *
     * @param orderId 조회하려는 주문 ID
     * @param customUserDetails 사용자 정보
     * @return {@link ResponseOrderDto} 객체
     */

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseOrderDto> getOrder(
            @PathVariable String orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                orderService.getOrder(orderId, customUserDetails.getId()));
    }

    /**
     * 여러 주문 조회
     * <p>'ROLE_CUSTOMER' - 본인 주문만 조회, 'ROLE_OWNER' - 본인 가게에 해당하는 주문만 조회,
     * 'ROLE_MANAGER' 이상 관리자는 모든 주문 조회<p/>
     *
     * @param status nullable 주문상태
     * @param search nullable 검색어
     * @param pageable default_sort - createdAt, updatedAt DESC
     * @param customUserDetails 사용자 정보
     * @return {@link Page}<{@link ResponseOrderDto}> 페이징 처리된 객체
     */

    @GetMapping
    public ResponseEntity<Page<ResponseOrderDto>> getOrders(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                orderService.getOrders(
                        status,
                        search,
                        pageable,
                        customUserDetails.getUsername(),
                        customUserDetails.getForContext().getRole()
                )
        );
    }

    /**
     * 주문 삭제
     * <p>권한에 따른 데이터 수정의 범위가 다름<p/>
     *
     * @param request 수정하려는 내용
     * @param orderId 수정하려는 주문의 ID
     * @param customUserDetails 사용자 정보
     * @return {@link ResponseOrderDto} 객체
     */

    @PatchMapping("/{orderId}")
    public ResponseEntity<ResponseOrderDto> updateOrder(
            @RequestBody RequestOrderUpdateDto request,
            @PathVariable String orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                orderService.updateOrder(
                        request,
                        orderId,
                        customUserDetails.getId(),
                        customUserDetails.getUsername(),
                        customUserDetails.getForContext().getRole()
                )
        );
    }

    /**
     * 'ROLE_MASTER' 권한만 데이터 삭제(숨김처리)가능
     *
     * @param orderId 숨김처리하려는 주문의 ID
     * @param customUserDetails 사용자 정보
     * @return Boolean 타입의 결과 : TURE - 성공
     * @exception NotFoundException 사용자를 찾지 못하면 예외 발생
     */

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<Boolean> deleteOrder(
            @PathVariable String orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                orderService.deleteOrder(orderId, customUserDetails.getUsername()));
    }

}
