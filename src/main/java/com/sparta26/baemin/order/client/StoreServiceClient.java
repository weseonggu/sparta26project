package com.sparta26.baemin.order.client;

import com.sparta26.baemin.store.entity.Store;

import java.util.UUID;

public interface StoreServiceClient {
    Store getStoreById(String storeId);
    UUID getStoreByMemberId(Long memberId);
}
