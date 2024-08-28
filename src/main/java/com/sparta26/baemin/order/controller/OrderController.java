package com.sparta26.baemin.order.controller;

import com.sparta26.baemin.dto.order.RequestOrderCreateDto;
import com.sparta26.baemin.dto.order.RequestOrderUpdateDto;
import com.sparta26.baemin.dto.order.ResponseOrderCreateDto;
import com.sparta26.baemin.dto.order.ResponseOrderDto;
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

    @PostMapping
    public ResponseEntity<ResponseOrderCreateDto> createOrder(
            @RequestBody RequestOrderCreateDto request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                orderService.createOrder(request, customUserDetails.getId()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseOrderDto> getOrder(
            @PathVariable String orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(
                orderService.getOrder(orderId, customUserDetails.getId()));
    }

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
