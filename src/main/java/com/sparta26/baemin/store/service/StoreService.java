package com.sparta26.baemin.store.service;

import com.sparta26.baemin.dto.store.RequestStoreDto;
import com.sparta26.baemin.dto.store.ResponseFindStoreDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.MemberNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.MemberStoreLimitExceededException;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UuidFormatException;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.repository.MemberRepository;
import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import com.sparta26.baemin.store.client.OperatingHoursClient;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final OperatingHoursClient operatingHoursClient;
    private final MemberRepository memberRepository;
    /**
     * 가게 생성, 가게주인만 생성가능
     */
    @Transactional
    public ResponseStoreDto createStore(RequestStoreDto requestStoreDto, Long memberId) {
        Optional<Store> findByMemberIdStore = storeRepository.findByMemberId(memberId);
        if (findByMemberIdStore.isPresent()) {
            throw new MemberStoreLimitExceededException("회원 당 1개의 가게만 등록 가능합니다.");
        }

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("not found member"));
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
                findMember);

        Store savedStore = storeRepository.save(store);

        return ResponseStoreDto.toDto(savedStore);
    }

    /**
     * id로 가게 단건 조회
     * @param storeId
     */
    public Page<ResponseFindStoreDto> findOneStore(String storeId, Pageable pageable) {

        if (!isValidUUID(storeId)) {
            log.error("Invalid UUID = {}",storeId);
            throw new UuidFormatException("Invalid UUID format");
        }

        Page<Store> pagingById = storeRepository.findByIdWithOperatingHours(UUID.fromString(storeId), pageable);

        return pagingById.map(ResponseFindStoreDto::new);
    }

    /**
     * 가게 전체 조회
     * @param pageable
     * @return
     */
    public Page<ResponseFindStoreDto> findAllStore(Pageable pageable) {
        Page<Store> pagingStore = storeRepository.findPagingAll(pageable);
        return pagingStore.map(ResponseFindStoreDto::new);
    }


    /**
     * 가게 수정
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
        Store store = storeRepository.findByMemberId(memberId).orElseThrow(() -> new StoreNotFoundException("not found store"));
        if (store.isActive()) {
            active = "현재 활성화 상태입니다.";
        }else {
            store.changeActive();
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

        Store store = storeRepository.findByMemberId(memberId).orElseThrow(() -> new StoreNotFoundException("not found store"));
        if (store.isActive()) {
            store.changeActive();
            active = "비활성화 완료!!";
        } else {
            active = "현재 비활성화 상태입니다.";
        }
        return active;
    }

    /**
     * 가게 삭제
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
            Store findStore = storeRepository.findByIdAndMemberId(UUID.fromString(storeId), memberId).orElseThrow(() -> new StoreNotFoundException("not found store"));
            List<OperatingHours> operatingHours = operatingHoursClient.findByStoreId(UUID.fromString(storeId));
            if (!operatingHours.isEmpty()) {
                for (OperatingHours operatingHour : operatingHours) {
                    operatingHour.delete(email);
                }
            }
            if (!findStore.isPublic()) {
                throw new IllegalArgumentException("현재 가게는 삭제되어 조회가 불가능합니다.");
            } else {
                findStore.delete(email);
            }
        } else {
            Store findStore = storeRepository.findById(UUID.fromString(storeId)).orElseThrow(() -> new StoreNotFoundException("not found store"));
            List<OperatingHours> operatingHours = operatingHoursClient.findByStoreId(UUID.fromString(storeId));

            if (!operatingHours.isEmpty()) {
                for (OperatingHours operatingHour : operatingHours) {
                    operatingHour.delete(email);
                }
            }

            if (!findStore.isPublic()) {
                throw new IllegalArgumentException("현재 가게는 삭제되어 조회가 불가능합니다.");
            }else {
                findStore.delete(email);
            }
        }
    }

    /**
     * storeId, memberId 로 조회
     * @param storeId
     * @param memberId
     * @return
     */
    public ResponseStoreDto findByIdAndMemberId(String storeId, Long memberId) {

        if (!isValidUUID(storeId)) {
            log.error("Invalid UUID = {}",storeId);
            throw new UuidFormatException("Invalid UUID format");
        }

        Store findStore = storeRepository.findByIdAndMemberId(UUID.fromString(storeId), memberId).orElseThrow(() ->
                new StoreNotFoundException("not found store"));

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
