package com.sparta26.baemin.product.repository;

import com.sparta26.baemin.dto.product.RequestSearchProductDto;
import com.sparta26.baemin.dto.product.ResponseProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ResponseProductDto> findAllProduct(Pageable pageable, RequestSearchProductDto condition);
}
