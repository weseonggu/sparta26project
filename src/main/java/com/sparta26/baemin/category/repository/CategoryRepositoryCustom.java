package com.sparta26.baemin.category.repository;

import com.sparta26.baemin.dto.category.RequestSearchCategoryDto;
import com.sparta26.baemin.dto.category.ResponseSearchCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryCustom {

    Page<ResponseSearchCategoryDto> findAllCategory(Pageable pageable, RequestSearchCategoryDto condition);
}
