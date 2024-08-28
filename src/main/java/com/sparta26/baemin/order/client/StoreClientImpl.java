package com.sparta26.baemin.order.client;

import com.sparta26.baemin.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreClientImpl implements StoreClient {

    Store store;

    //    private final StoreService storeService;

    public Store getStoreById(String storeId) {
        //TODO StoreService 에서 storeId 로 가게정보를 가져오는 메서드
        return store;
    }
}
