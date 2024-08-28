package com.sparta26.baemin.order.client;

import com.sparta26.baemin.store.entity.Store;

public interface OrderStoreClient {
    Store getStoreById(String storeId);
}
