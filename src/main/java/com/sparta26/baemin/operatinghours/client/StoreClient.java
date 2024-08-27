package com.sparta26.baemin.operatinghours.client;

import com.sparta26.baemin.store.entity.Store;

import java.util.Optional;

public interface StoreClient {

    Optional<Store> findByIdAndMemberId(String storeId, Long memberId);
}
