package com.sparta26.baemin.operatinghours.client;

import com.sparta26.baemin.exception.exceptionsdefined.UuidFormatException;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreClientImpl implements StoreClient{

    private final StoreRepository storeRepository;

    @Override
    public Optional<Store> findByIdAndMemberId(String storeId, Long memberId) {
        if (!isValidUUID(storeId)) {
            log.error("Invalid UUID = {}",storeId);
            throw new UuidFormatException("Invalid UUID format");
        }

        return storeRepository.findByIdAndMemberId(UUID.fromString(storeId), memberId);

    }

    /**
     * UUID 형식 검증 메서드
     * @param aiId
     * @return
     */
    public boolean isValidUUID(String aiId) {
        try {
            UUID.fromString(aiId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
