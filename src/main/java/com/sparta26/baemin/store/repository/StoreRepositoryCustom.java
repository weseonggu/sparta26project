package com.sparta26.baemin.store.repository;

import com.sparta26.baemin.dto.store.RequestSearchStoreDto;
import com.sparta26.baemin.dto.store.ResponseSearchStoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StoreRepositoryCustom {
    ResponseSearchStoreDto findOneStore(UUID storeId);

    Page<ResponseSearchStoreDto> findPagingAll(Pageable pageable, RequestSearchStoreDto condition);
}
