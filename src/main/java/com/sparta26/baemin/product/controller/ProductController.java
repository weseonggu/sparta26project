package com.sparta26.baemin.product.controller;

import com.sparta26.baemin.dto.product.RequestProductDto;
import com.sparta26.baemin.dto.product.ResponseProductDto;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.jwt.ForContext;
import com.sparta26.baemin.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 등록, 가게 주인만 허용
     * @param request
     * @param customUserDetails
     * @param storeId
     * @return
     */
    @PostMapping("/products/{storeId}")
    public ResponseEntity<?> createProduct(@RequestBody RequestProductDto request,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable("storeId") String storeId) {

        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String email = context.getEmail();

        ResponseProductDto response = productService.createProduct(request, storeId, memberId, email);
        return ResponseEntity.ok(response);
    }
}