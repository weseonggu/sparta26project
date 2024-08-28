package com.sparta26.baemin.operatinghours.service;

import com.sparta26.baemin.dto.operatinghours.RequestOperatingHoursDto;
import com.sparta26.baemin.dto.operatinghours.ResponseOperatingDto;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UuidFormatException;
import com.sparta26.baemin.operatinghours.client.StoreClient;
import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import com.sparta26.baemin.operatinghours.repository.OperatingHoursRepository;
import com.sparta26.baemin.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OperatingHoursService {

    private final OperatingHoursRepository operatingHoursRepository;
    private final StoreClient storeClient;


    /**
     * 가게 운영시간 등록, 가게주인만 생성가능
     * @param requestOperatingHoursDto
     * @param storeId
     * @param memberId
     * @param email
     * @return
     */
    @Transactional
    public ResponseOperatingDto createOperatingHours(RequestOperatingHoursDto requestOperatingHoursDto, String storeId, Long memberId, String email) {

        Store store = storeClient.findByIdAndMemberId(storeId, memberId).orElseThrow(()
                -> new StoreNotFoundException("본인의 상점만 등록 가능합니다."));

        if (!isValidUUID(storeId)) {
            log.error("UUID = {}", storeId);
            throw new UuidFormatException("UUID 형식이 틀렸습니다.");
        }

        OperatingHours findOperating = operatingHoursRepository.findByStoreIdAndOpenDays(UUID.fromString(storeId),
                requestOperatingHoursDto.getOpen_days());

        if (findOperating != null) {
            log.error("등록 시도한 요일 : {}", requestOperatingHoursDto.getOpen_days());
            throw new IllegalArgumentException(requestOperatingHoursDto.getOpen_days() + "은 이미 등록되어있습니다.");
        }

        OperatingHours operatingHours = new OperatingHours(requestOperatingHoursDto.getOpening_time(),
                requestOperatingHoursDto.getClosing_time(),
                requestOperatingHoursDto.getOpen_days(),
                requestOperatingHoursDto.getLast_order(),
                store,
                email);

        OperatingHours savedOperating = operatingHoursRepository.save(operatingHours);
        return ResponseOperatingDto.toDto(savedOperating);
    }

    /**
     * 가게 운영시간 수정
     * @param requestOperatingHoursDto
     * @param operatingId
     * @param memberId
     * @param email
     * @return
     */
    @Transactional
    public ResponseOperatingDto updateOperatingHours(RequestOperatingHoursDto requestOperatingHoursDto, String operatingId, Long memberId, String email, String role) {
        if (!isValidUUID(operatingId)) {
            log.error("UUID = {}", operatingId);
            throw new UuidFormatException("UUID 형식이 틀렸습니다.");
        }

        OperatingHours updateOperating;
        if (role.equals("ROLE_OWNER")) {
            OperatingHours findOperating = operatingHoursRepository.findByIdAndCreatedBy(UUID.fromString(operatingId), email)
                    .orElseThrow(() -> new IllegalArgumentException("not found OperatingHours or Invalid user"));

            updateOperating = findOperating.update(requestOperatingHoursDto, email);

        } else {

            OperatingHours findOperating = operatingHoursRepository.findById(UUID.fromString(operatingId)).orElseThrow(() ->
                    new IllegalArgumentException("not found OperatingHours"));
            updateOperating = findOperating.update(requestOperatingHoursDto, email);
        }

        return ResponseOperatingDto.toDto(updateOperating);
    }

    /**
     * 가게 운영시간 삭제
     * @param operatingId
     * @param email
     */
    @Transactional
    public void deleteOperatingHours(String operatingId, String email, String role) {
        if (!isValidUUID(operatingId)) {
            log.error("UUID = {}", operatingId);
            throw new UuidFormatException("UUID 형식이 틀렸습니다.");
        }

        if (role.equals("ROLE_OWNER")) {
            OperatingHours findOperating = operatingHoursRepository.findByIdAndCreatedBy(UUID.fromString(operatingId), email)
                    .orElseThrow(() -> new IllegalArgumentException("not found OperatingHours or Invalid user"));
            findOperating.delete(email);
        } else {
            OperatingHours findOperating = operatingHoursRepository.findById(UUID.fromString(operatingId)).orElseThrow(() ->
                    new IllegalArgumentException("not found OperatingHours"));
            findOperating.delete(email);
        }
    }

    /**
     * 가게 운영시간 단건 조회
     * @param storeId
     * @return
     */
    public List<ResponseOperatingDto> findOneOperatingHours(String storeId) {
        if (!isValidUUID(storeId)) {
            log.error("UUID = {}", storeId);
            throw new UuidFormatException("UUID 형식이 틀렸습니다.");
        }

        List<OperatingHours> operatingHoursList = operatingHoursRepository.findByStoreId(UUID.fromString(storeId));
        List<ResponseOperatingDto> operatingHoursDtoList = new ArrayList<>();

        for (OperatingHours operatingHours : operatingHoursList) {
            operatingHoursDtoList.add(ResponseOperatingDto.toDto(operatingHours));
        }
        return operatingHoursDtoList;
    }

    /**
     * UUID 형식 검증 메서드
     * @param id
     * @return
     */
    public boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }



}
