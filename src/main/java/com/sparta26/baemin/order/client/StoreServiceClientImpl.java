package com.sparta26.baemin.order.client;

import com.sparta26.baemin.dto.store.ResponseFindStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.ClientException;
import com.sparta26.baemin.exception.exceptionsdefined.NotFoundException;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceClientImpl implements StoreServiceClient {

        private final StoreService storeService;
    public Store getStoreById(String storeId) {

        try {
            Page<ResponseFindStoreDto> oneStore =
                    storeService.findOneStore(storeId, Pageable.unpaged());

            return convertToStore(oneStore.getContent().get(0));
        } catch (IndexOutOfBoundsException e) {
            log.warn("[FAIL] getStoreById : " + storeId);
            throw new ClientException("The external service returned an error");
        }
    }

    private Store convertToStore(ResponseFindStoreDto response) {
        return Store.createStoreWithId(
                UUID.fromString(response.getId()),
                response.getName(),
                response.getDescription(),
                response.getAddress(),
                response.getPhone_number(),
                response.is_active()
        );
    }
}
