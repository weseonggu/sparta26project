package com.sparta26.baemin.product.controller;

import com.sparta26.baemin.dto.product.RequestProductDto;
import com.sparta26.baemin.dto.product.RequestProductWithoutStockDto;
import com.sparta26.baemin.dto.product.RequestSearchProductDto;
import com.sparta26.baemin.dto.product.ResponseProductDto;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.jwt.ForContext;
import com.sparta26.baemin.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 등록, 가게 주인만 허용
     *
     * @param request
     * @param customUserDetails
     * @param storeId
     * @return
     */
    @PostMapping("/products/{storeId}")
    public ResponseEntity<?> createProduct(@RequestBody RequestProductDto request,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable("storeId") UUID storeId) {
        log.info("상품 등록 시도 중 | memberId = {}, storeId = {}", customUserDetails.getForContext().getId(), storeId);

        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String email = context.getEmail();

        ResponseProductDto response = productService.createProduct(request, storeId, memberId, email);

        log.info("상품 등록 완료 | responseDto = {}", response);
        return ResponseEntity.ok(response);
    }


    /**
     * 개별 상품 수정 | 가게 주인, 관리자
     *
     * @param request
     * @param customUserDetails
     * @param productId
     * @return
     */
    @PatchMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ROLE_MASTER', 'ROLE_OWNER')")
    public ResponseEntity<?> updateProduct(@RequestBody RequestProductWithoutStockDto request,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable("productId") UUID productId) {
        log.info("상품 수정 시도 중 | memberId = {}, productId = {}", customUserDetails.getForContext().getId(), productId);

        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();
        String email = context.getEmail();
        String role = context.getRole();

        ResponseProductDto response = productService.updateProduct(request, memberId, email, role, productId);
        log.info("상품 수정 완료 | responseDto = {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * 개별 상품 조회
     *
     * @param productId
     * @return
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> findOneProduct(@PathVariable UUID productId) {
        log.info("상품 조회 시도 중 | productId = {}", productId);

        ResponseProductDto findProduct = productService.findOneProduct(productId);

        log.info("상품 조회 완료 | findProduct = {}", findProduct);
        return ResponseEntity.ok(findProduct);
    }

    /**
     * 전체 상품 조회
     * @param request
     * @param pageable
     * @return
     */
    @GetMapping("/products")
    public ResponseEntity<?> findAllProducts(RequestSearchProductDto request,
                                             @PageableDefault(size = 10) Pageable pageable) {
        log.info("전체 상품 조회 | requestDto = {}", request);

        Page<ResponseProductDto> allProducts = productService.findAllProducts(request, pageable);

        log.info("전제 상품 조회 완료");
        return ResponseEntity.ok(allProducts);
    }

    /**
     * 개별 상품 삭제 | 가게 주인, 관리자
     * @param productId
     * @param customUserDetails
     * @return
     */
    @DeleteMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ROLE_MASTER', 'ROLE_OWNER')")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("상품 삭제 시도 중 | productId = {}, memberId = {}", productId, customUserDetails.getForContext().getId());
        
        ForContext context = customUserDetails.getForContext();
        String email = context.getEmail();
        String role = context.getRole();

        productService.deleteProduct(productId, email, role);
        log.info("상품 삭제 완료 | productId = {}, memberId = {}", productId, customUserDetails.getForContext().getId());
        return ResponseEntity.ok("삭제 완료");
    }


}
