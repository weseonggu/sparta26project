package com.sparta26.baemin.order.client;

import com.sparta26.baemin.store.entity.Store;

public interface StoreClient {
    Store getStoreById(String storeId);
}
