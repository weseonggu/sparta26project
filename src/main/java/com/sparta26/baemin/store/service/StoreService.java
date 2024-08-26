package com.sparta26.baemin.store.service;

import com.sparta26.baemin.dto.store.RequestStoreDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.MemberNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.MemberStoreLimitExceededException;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UuidFormatException;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.repository.MemberRepository;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /**
     * 가게 생성
     */
    @Transactional
    public ResponseStoreDto createStore(RequestStoreDto requestStoreDto, Long memberId) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("없는 회원입니다."));

        Store findStore = storeRepository.findByMemberId(memberId).orElse(null);
        if (findStore != null) {
            throw new MemberStoreLimitExceededException("회원 당 1개의 가게만 등록 가능합니다.");
        }

        Store store = Store.createStore(requestStoreDto.getName(),
                requestStoreDto.getDescription(),
                requestStoreDto.getOpening_time(),
                requestStoreDto.getClosing_time(),
                requestStoreDto.getOpen_days(),
                requestStoreDto.getAddress(),
                requestStoreDto.getPhone_number(),
                findMember);

        Store savedStore = storeRepository.save(store);

        return ResponseStoreDto.toDto(savedStore);
    }

    /**
     * id로 가게 단건 조회
     * @param storeId
     */
    public ResponseStoreDto findOneStore(String storeId) {

        if (!isValidUUID(storeId)) {
            throw new UuidFormatException("Invalid UUID format");
        }

        Store findStore = storeRepository.findById(UUID.fromString(storeId)).orElseThrow(() -> new StoreNotFoundException("store not found"));
        return ResponseStoreDto.toDto(findStore);
    }

    /**
     * UUID 형식 검증 메서드
     * @param storeId
     * @return
     */
    public boolean isValidUUID(String storeId) {
        try {
            UUID.fromString(storeId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
