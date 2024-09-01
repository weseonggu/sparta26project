package com.sparta26.baemin.category.service;

import com.sparta26.baemin.category.entity.Category;
import com.sparta26.baemin.category.repository.CategoryRepository;
import com.sparta26.baemin.dto.category.RequestCategoryDto;
import com.sparta26.baemin.dto.category.RequestSearchCategoryDto;
import com.sparta26.baemin.dto.category.ResponseCategoryDto;
import com.sparta26.baemin.dto.category.ResponseSearchCategoryDto;
import com.sparta26.baemin.exception.exceptionsdefined.CategoryNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    /**
     * 카테고리 등록 | 가게 주인
     * @param request
     * @param storeId
     * @param memberId
     * @return
     */
    @Transactional
    public ResponseCategoryDto createCategory(RequestCategoryDto request, UUID storeId, Long memberId) {

        Store findStore = storeRepository.findByIdAndMemberId(storeId, memberId).orElseThrow(() ->
                new StoreNotFoundException("등록된 가게가 없거나 본인 상점의 카테고리만 등록 가능합니다."));

        Optional<Category> findCategory = categoryRepository.findByStoreIdAndName(storeId, request.getName());
        if (findCategory.isPresent()) {
            throw new IllegalArgumentException("중복된 카테고리가 존재합니다");
        }

        Category savedCategory = categoryRepository.save(Category.createCategory(request.getName(), findStore));

        return ResponseCategoryDto.toDto(savedCategory);
    }

    /**
     * 카테고리 수정 | 가게 주인, 관리자
     * @param request
     * @param categoryId
     * @param email
     * @param role
     * @return
     */
    @Transactional
    public ResponseCategoryDto updateCategory(RequestCategoryDto request, UUID categoryId, String email, String role) {
        Category category;

        if (role.equals("ROLE_OWNER")) {
            Category findCategory = categoryRepository.findByIdAndCreatedBy(categoryId, email).orElseThrow(() ->
                    new CategoryNotFoundException("등록된 카테고리가 없거나 본인 상점의 카테고리만 등록 가능합니다."));

            List<Category> categories= categoryRepository.findDuplicatedByStoreIdAndName(findCategory.getStore().getId(), findCategory.getName());

            for (Category category1 : categories) {

                if (category1.getName().equals(request.getName())) {
                    log.error("중복된 카테고리명이 이미 존재합니다. name = " + request.getName());
                    throw new IllegalArgumentException(category1.getName()+" 카테고리명이 이미 존재합니다.");
                }
            }

            category = findCategory.update(request);
        } else {
            Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new CategoryNotFoundException("not found category"));

            List<Category> categories= categoryRepository.findDuplicatedByStoreIdAndName(findCategory.getStore().getId(), findCategory.getName());

            for (Category category1 : categories) {
                if (category1.getName().equals(request.getName())) {
                    log.error("중복된 카테고리명이 이미 존재합니다. name = " + request.getName());
                    throw new IllegalArgumentException(category1.getName()+" 카테고리명이 이미 존재합니다.");
                }
            }

            category = findCategory.update(request);
        }

        return ResponseCategoryDto.toDto(category);
    }

    /**
     * 카테고리 삭제 | 가게 주인, 관리자
     * @param categoryId
     * @param email
     * @param role
     */
    @Transactional
    public void deleteCategory(UUID categoryId, String email, String role) {

        if (role.equals("ROLE_OWNER")) {
            Category findCategory = categoryRepository.findByIdAndCreatedBy(categoryId, email).orElseThrow(() ->
                    new CategoryNotFoundException("등록된 카테고리가 없거나 본인 상점의 카테고리만 삭제 가능합니다."));

            findCategory.delete(email);
        } else {
            Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new CategoryNotFoundException("not found category"));

            findCategory.delete(email);
        }
    }

    /**
     * 카테고리 전체 조회 | 관리자
     * @param pageable
     * @param condition
     * @return
     */
    public Page<ResponseSearchCategoryDto> findAllCategory(Pageable pageable, RequestSearchCategoryDto condition) {
        return categoryRepository.findAllCategory(pageable, condition);
    }
}
