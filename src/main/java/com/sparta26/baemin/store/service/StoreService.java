package com.sparta26.baemin.store.service;

import com.sparta26.baemin.category.repository.CategoryRepository;
import com.sparta26.baemin.deliveryzone.repository.DeliveryZoneRepository;
import com.sparta26.baemin.dto.store.RequestSearchStoreDto;
import com.sparta26.baemin.dto.store.RequestStoreDto;
import com.sparta26.baemin.dto.store.ResponseSearchStoreDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.MemberStoreLimitExceededException;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UuidFormatException;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.operatinghours.repository.OperatingHoursRepository;
import com.sparta26.baemin.product.repository.ProductRepository;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final OperatingHoursRepository operatingHoursRepository;
    private final CategoryRepository categoryRepository;
    private final DeliveryZoneRepository deliveryZoneRepository;
    private final ProductRepository productRepository;

    /**
     * 가게 생성, 가게주인만 생성가능
     */
    @Transactional
    public ResponseStoreDto createStore(RequestStoreDto requestStoreDto, Long memberId) {
        Optional<Store> findByMemberIdStore = storeRepository.findByMemberId(memberId);
        if (findByMemberIdStore.isPresent()) {
            throw new MemberStoreLimitExceededException("회원 당 1개의 가게만 등록 가능합니다.");
        }
        Member member = new Member(memberId);

        Store findStore = storeRepository.findByAddressOrPhoneNumberAndMemberIdNot(requestStoreDto.getAddress(), requestStoreDto.getPhone_number(), memberId).orElse(null);


        log.info("Store = {}, memberId = {}", findStore, memberId);

        if (findStore != null) {
            if (findStore.getAddress().equals(requestStoreDto.getAddress())) {
                throw new IllegalArgumentException("중복된 주소입니다.");
            }
            if (findStore.getPhoneNumber().equals(requestStoreDto.getPhone_number())) {
                throw new IllegalArgumentException("중복된 전화번호입니다.");
            }
        }

        Store store = Store.createStore(requestStoreDto.getName(),
                requestStoreDto.getDescription(),
                requestStoreDto.getAddress(),
                requestStoreDto.getPhone_number(),
                member);

        Store savedStore = storeRepository.save(store);

        return ResponseStoreDto.toDto(savedStore);
    }

    /**
     * id로 가게 단건 조회
     * @param storeId
     */
    public ResponseSearchStoreDto findOneStore(UUID storeId) {
        return storeRepository.findOneStore(storeId);
    }

    /**
     * 가게 전체 조회
     * @param pageable
     * @return
     */
    public Page<ResponseSearchStoreDto> findAllStore(Pageable pageable, RequestSearchStoreDto condition) {
        return storeRepository.findPagingAll(pageable, condition);
    }


    /**
     * 가게 수정, 가게주인 관리자 가능
     * @param requestStoreDto
     * @param storeId
     * @param memberId
     * @param email
     * @return
     */
    @Transactional
    public ResponseStoreDto updateStore(RequestStoreDto requestStoreDto, String storeId, Long memberId, String email, String role) {

        Store validStore = storeRepository.findByAddressOrPhoneNumberAndMemberIdNot(requestStoreDto.getAddress(), requestStoreDto.getPhone_number(), memberId).orElse(null);

        log.info("Store = {}, memberId = {}", validStore, memberId);

        if (validStore != null) {
            if (validStore.getAddress().equals(requestStoreDto.getAddress())) {
                throw new IllegalArgumentException("중복된 주소입니다.");
            }
            if (validStore.getPhoneNumber().equals(requestStoreDto.getPhone_number())) {
                throw new IllegalArgumentException("중복된 전화번호입니다.");
            }
        }

        if (!isValidUUID(storeId)) {
            log.error("Invalid UUID = {}",storeId);
            throw new UuidFormatException("Invalid UUID format");
        }

        Store updateStore;

        if (role.equals("ROLE_OWNER")) {
            Store store = storeRepository.findByIdAndMemberId(UUID.fromString(storeId), memberId).orElseThrow(() ->
                    new StoreNotFoundException("등록된 상점이 없거나 본인의 가게만 수정이 가능합니다."));
            updateStore = store.update(requestStoreDto, email);
        } else {
            Store store = storeRepository.findById(UUID.fromString(storeId)).orElseThrow(() ->
                    new StoreNotFoundException("not found store"));
            updateStore = store.update(requestStoreDto, email);
        }

        return ResponseStoreDto.toDto(updateStore);
    }

    /**
     * 가게 활성화
     * @param memberId
     */
    @Transactional
    public String activeStore(Long memberId) {
        String active = null;
        Store store = storeRepository.findByMemberId(memberId).orElseThrow(() -> new StoreNotFoundException("not found store or Invalid user"));
        if (store.isActive()) {
            active = "현재 활성화 상태입니다.";
        }else {
            store.changeActive(true);
            active = "활성화 완료!!";
        }
        return active;
    }

    /**
     * 가게 비활성화
     * @param memberId
     * @return
     */
    @Transactional
    public String inactiveStore(Long memberId) {
        String active = null;

        Store store = storeRepository.findByMemberId(memberId).orElseThrow(() -> new StoreNotFoundException("not found store or Invalid user"));
        if (store.isActive()) {
            store.changeActive(false);
            active = "비활성화 완료!!";
        } else {
            active = "현재 비활성화 상태입니다.";
        }
        return active;
    }

    /**
     * 가게 삭제, 가게 주인 관리자 가능
     * @param storeId
     * @param email
     */
    @Transactional
    public void deleteStore(String storeId, String email, String role, Long memberId) {
        if (!isValidUUID(storeId)) {
            log.error("Invalid UUID = {}",storeId);
            throw new UuidFormatException("Invalid UUID format");
        }

        if (role.equals("ROLE_OWNER")) {
            Store findStore = storeRepository.findByIdAndMemberId(UUID.fromString(storeId), memberId).orElseThrow(() -> new StoreNotFoundException("not found store or 본인의 가게만 삭제 가능합니다."));

            // 가게 false 변환
            if (!findStore.isPublic()) {
                throw new IllegalArgumentException("현재 가게는 삭제되어 조회가 불가능합니다.");
            }
                storeRepository.deleteByIdAndMemberId(UUID.fromString(storeId), memberId, email);

            // 운영시간 false 변환
                operatingHoursRepository.deleteByStoreId(UUID.fromString(storeId), email);
            // 카테고리 false 변환
                categoryRepository.deleteByStoreId(storeId, email);
            // 배달지역 false 변환
                deliveryZoneRepository.deleteByStoreId(storeId, email);
            // 상품 false 변환
                productRepository.deleteByStoreId(storeId, email);
        } else {
            Store findStore = storeRepository.findById(UUID.fromString(storeId)).orElseThrow(() -> new StoreNotFoundException("not found store"));

            if (!findStore.getOperatingHours().isEmpty()) {
                operatingHoursRepository.deleteByStoreId(UUID.fromString(storeId), email);
            }

            if (!findStore.isPublic()) {
                throw new IllegalArgumentException("현재 가게는 삭제되어 조회가 불가능합니다.");
            }else {
                storeRepository.deleteById(UUID.fromString(storeId), email);

                // 운영시간 false 변환
                    operatingHoursRepository.deleteByStoreId(UUID.fromString(storeId), email);
                // 카테고리 false 변환
                    categoryRepository.deleteByStoreId(storeId, email);
                // 배달지역 false 변환
                    deliveryZoneRepository.deleteByStoreId(storeId, email);
                // 상품 false 변환
                    productRepository.deleteByStoreId(storeId, email);
            }
        }
    }

    /**
     * 멤버 ID로 조회 | 컨트롤러 없음
     * @param memberId
     * @return
     */
    public ResponseStoreDto findByMemberId(Long memberId) {
        Store store = storeRepository.findByMemberId(memberId).orElseThrow(() -> new StoreNotFoundException("not found store"));
        return ResponseStoreDto.toDto(store);
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
