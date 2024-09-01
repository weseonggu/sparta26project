package com.sparta26.baemin.category.controller;

import com.sparta26.baemin.category.service.CategoryService;
import com.sparta26.baemin.dto.category.RequestCategoryDto;
import com.sparta26.baemin.dto.category.RequestSearchCategoryDto;
import com.sparta26.baemin.dto.category.ResponseCategoryDto;
import com.sparta26.baemin.dto.category.ResponseSearchCategoryDto;
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
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 등록 | 가게 주인
     *
     * @param request
     * @param storeId
     * @param customUserDetails
     * @return
     */
    @PostMapping("/category/{storeId}")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_OWNER')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody RequestCategoryDto request,
                                            @PathVariable("storeId") UUID storeId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("카테고리 등록 시도 중 | storeId = {}, memberId = {}, request = {}", storeId, customUserDetails.getForContext().getId(), request);

        ForContext context = customUserDetails.getForContext();
        Long memberId = context.getId();

        ResponseCategoryDto response = categoryService.createCategory(request, storeId, memberId);
        log.info("카테고리 등록 완료 | storeId = {}, memberId = {}, request = {}", storeId, customUserDetails.getForContext().getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리 수정 | 가게 주인, 관리자
     *
     * @param request
     * @param categoryId
     * @param customUserDetails
     * @return
     */
    @PatchMapping("/category/{categoryId}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ROLE_OWNER', 'ROLE_MASTER')")
    public ResponseEntity<?> updateCategory(@RequestBody RequestCategoryDto request,
                                            @PathVariable("categoryId") UUID categoryId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("카테고리 등록 시도 중 | categoryId = {}, email = {}, request = {}", categoryId, customUserDetails.getForContext().getEmail(), request);

        ForContext context = customUserDetails.getForContext();
        String email = customUserDetails.getForContext().getEmail();
        String role = customUserDetails.getForContext().getRole();

        ResponseCategoryDto response = categoryService.updateCategory(request, categoryId, email, role);
        log.info("카테고리 등록 완료 | categoryId = {}, email = {}, request = {}", categoryId, customUserDetails.getForContext().getEmail(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리 삭제 | 가게 주인, 관리자
     *
     * @param categoryId
     * @param customUserDetails
     * @return
     */
    @DeleteMapping("/category/{categoryId}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ROLE_OWNER', 'ROLE_MASTER')")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") UUID categoryId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("카테고리 삭제 시도 중 | categoryId = {}, email = {}", categoryId, customUserDetails.getForContext().getEmail());

        ForContext context = customUserDetails.getForContext();
        String email = customUserDetails.getForContext().getEmail();
        String role = customUserDetails.getForContext().getRole();

        categoryService.deleteCategory(categoryId, email, role);
        log.info("카테고리 삭제 완료 | categoryId = {}, email = {}", categoryId, customUserDetails.getForContext().getEmail());
        return ResponseEntity.ok("삭제 완료");
    }

    /**
     * 카테고리 전체 조회 | 관리자
     * @param customUserDetails
     * @param pageable
     * @param condition
     * @return
     */
    @GetMapping("/category")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_MASTER')")
    public ResponseEntity<?> findAllCategory(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             Pageable pageable,
                                             RequestSearchCategoryDto condition) {
        log.info("카테고리 전체 조회 시도 중 | memberId = {}", customUserDetails.getId());

        Page<ResponseSearchCategoryDto> result = categoryService.findAllCategory(pageable, condition);
        log.info("카테고리 전체 조회 성공");
        return ResponseEntity.ok(result);
    }
}
