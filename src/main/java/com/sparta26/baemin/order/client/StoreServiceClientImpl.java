package com.sparta26.baemin.order.client;

import com.sparta26.baemin.dto.store.ResponseSearchStoreDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.ClientException;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceClientImpl implements StoreServiceClient {

    private final StoreService storeService;
    public Store getStoreById(String storeId) {

        try {
            ResponseSearchStoreDto responseSearchStoreDto =
                    storeService.findOneStore(UUID.fromString(storeId));

            return convertToStore(responseSearchStoreDto);
        } catch (Exception e) {
            log.warn(
                    "[The external storeService returned an error] getStoreById : "
                            + storeId
            );
            throw new ClientException("Store not found.");
        }
    }

    public UUID  getStoreByMemberId(Long memberId) {

        try {
            ResponseStoreDto responseStoreDto =
                    storeService.findByMemberId(memberId);

            return UUID.fromString(responseStoreDto.getId());
        } catch (Exception e) {
            log.warn(
                    "[The external storeService returned an error] getStoreByMemberId : "
                            + memberId
            );
            throw new ClientException("Store not found.");
        }
    }

    private Store convertToStore(ResponseSearchStoreDto response) {
        return Store.createStoreWithId(
                response.getStore_id(),
                response.getName(),
                response.getDescription(),
                response.getAddress(),
                response.getPhone_number(),
                response.is_active()
        );
    }
}