package com.sparta26.baemin.product.clinet;

import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductToStoreClientImpl implements ProductToStoreClient {

    private final StoreService storeService;

    @Override
    public ResponseStoreDto findByIdAndMemberId(String storeId, Long memberId) {
        return storeService.findByIdAndMemberId(storeId, memberId);
    }
}
