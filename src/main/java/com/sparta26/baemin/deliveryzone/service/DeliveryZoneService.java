package com.sparta26.baemin.deliveryzone.service;

import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import com.sparta26.baemin.deliveryzone.repository.DeliveryZoneRepository;
import com.sparta26.baemin.dto.deliveryzone.RequestDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.RequestSearchDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseSearchDeliveryDto;
import com.sparta26.baemin.exception.exceptionsdefined.DeliveryZoneNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
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
public class DeliveryZoneService {

    private final DeliveryZoneRepository deliveryZoneRepository;
    private final StoreRepository storeRepository;

    /**
     * 배달 지역 생성 | 가게 주인
     * @param storeId
     * @param request
     * @param memberId
     * @return
     */
    @Transactional
    public ResponseDeliveryDto createDeliveryZone(UUID storeId, RequestDeliveryDto request, Long memberId) {

        Store store = storeRepository.findByIdAndMemberId(storeId, memberId).orElseThrow(() ->
                new StoreNotFoundException("등록된 본인의 가게를 찾을 수 없거나 본인의 배달 지역만 생성 가능합니다"));

        Optional<DeliveryZone> findCategory = deliveryZoneRepository.findByStoreIdAndName(storeId, request.getName());
        if (findCategory.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 배달 지역입니다.");
        }

        DeliveryZone deliveryZone = DeliveryZone.createDeliveryZone(request.getName(),
                store);

        DeliveryZone savedDelivery = deliveryZoneRepository.save(deliveryZone);

        return ResponseDeliveryDto.toDto(savedDelivery);
    }


    /**
     * 배달 지역 수정 | 가게 주인, 관리자
     * @param deliveryId
     * @param request
     * @param email
     * @param role
     * @return
     */
    @Transactional
    public ResponseDeliveryDto updateDeliveryZone(UUID deliveryId, RequestDeliveryDto request, String email, String role) {
        DeliveryZone deliveryZone;

        if (role.equals("ROLE_OWNER")) {
            DeliveryZone findDelivery = deliveryZoneRepository.findByIdAndCreatedBy(deliveryId, email).orElseThrow(() ->
                    new DeliveryZoneNotFoundException("등록된 배달지역을 찾을 수 없거나 본인 가게의 배달 지역만 수정 가능합니다."));

            List<DeliveryZone> findList = deliveryZoneRepository.findDuplicatedByStoreIdAndName(findDelivery.getStore().getId(), findDelivery.getName());

            for (DeliveryZone dz : findList) {
                System.out.println("결과 = " + dz.getName());
                if (dz.getName().equals(request.getName())) {
                    log.error("중복된 배달지역이 이미 존재합니다. name = " + request.getName());
                    throw new IllegalArgumentException(dz.getName()+" 배달지역이 이미 존재합니다.");
                }
            }

            deliveryZone = findDelivery.update(request);
        } else {
            DeliveryZone findDelivery = deliveryZoneRepository.findById(deliveryId).orElseThrow(() ->
                    new DeliveryZoneNotFoundException("not found delivery_zone"));

            List<DeliveryZone> findList = deliveryZoneRepository.findDuplicatedByStoreIdAndName(findDelivery.getStore().getId(), findDelivery.getName());

            for (DeliveryZone dz : findList) {
                if (dz.getName().equals(request.getName())) {
                    log.error("중복된 배달지역이 이미 존재합니다. name = " + request.getName());
                    throw new IllegalArgumentException(dz.getName()+" 배달지역이 이미 존재합니다.");
                }
            }

            deliveryZone = findDelivery.update(request);
        }
        return ResponseDeliveryDto.toDto(deliveryZone);
    }

    /**
     * 배달 지역 삭제 | 가게 주인, 관리자
     * @param deliveryId
     * @param email
     * @param role
     * @return
     */
    @Transactional
    public String deleteDeliveryZone(UUID deliveryId, String email, String role) {

        if (role.equals("ROLE_OWNER")) {
            DeliveryZone findDelivery = deliveryZoneRepository.findByIdAndCreatedBy(deliveryId, email).orElseThrow(() ->
                    new DeliveryZoneNotFoundException("등록된 배달지역을 찾을 수 없거나 본인 가게의 배달 지역만 삭제 가능합니다."));

            findDelivery.delete(email);
        } else {
            DeliveryZone findDelivery = deliveryZoneRepository.findById(deliveryId).orElseThrow(() ->
                    new DeliveryZoneNotFoundException("not found delivery_zone"));
            findDelivery.delete(email);
        }
        return "삭제 완료";        
    }

    /**
     * 배달 지역 전체 조회 | 관리자
     * @param condition
     * @param pageable
     * @return
     */
    public Page<ResponseSearchDeliveryDto> findAllDeliveryZone(RequestSearchDeliveryDto condition, Pageable pageable) {

        return deliveryZoneRepository.findAllDelivery(condition, pageable);
    }
}
