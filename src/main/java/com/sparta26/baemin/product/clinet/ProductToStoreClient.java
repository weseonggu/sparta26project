package com.sparta26.baemin.product.clinet;

import com.sparta26.baemin.dto.store.ResponseStoreDto;

public interface ProductToStoreClient {

    ResponseStoreDto findByIdAndMemberId(String storeId, Long memberId);
}
