package com.sparta26.baemin.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sparta26.baemin.address.service.AddressService;
import com.sparta26.baemin.dto.address.RequestAddressDto;
import com.sparta26.baemin.dto.address.ResponseAddressDto;
import com.sparta26.baemin.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final ObjectMapper objectMapper;

    /**
     * 주소 등록 일반 사용자만 접근 가능
     * @return
     */
    @PostMapping("v1/address")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addAddress(@Valid @RequestBody RequestAddressDto requestAddressDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResponseAddressDto dto = new ResponseAddressDto(addressService.createAdressInfo(requestAddressDto, userDetails));
        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(
                "AddressFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("zipCode","roadAddress", "roadAddressEnglish","id")
        );

        objectMapper.setFilterProvider(filters);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * 주소 조회 일반 사용자와 매니저 만 접근 가능
     * @param member_id
     * @return
     */
    @GetMapping("v1/address/{member_id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<Page<?>> getAddress(@PathVariable("member_id") int member_id,
                                              @PageableDefault(size =10) Pageable pageable,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        addressService.getAddress(member_id, userDetails);
        return null;
    }

    /**
     * 주소 정보 변경 사용자와 매니저 만 접근 가능
     * @param requestAddressDto
     * @return
     */
    @PutMapping("v1/adress/update")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    public ResponseEntity<?> updateAddress(@Valid @RequestBody RequestAddressDto requestAddressDto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        addressService.updateAddress(userDetails, requestAddressDto);
        return null;
    }

    /**
     * 주소 데이터 비공개 처리 사용자 매니저 마스터 가능
     * @param requestAddressDto
     * @return
     */
    @DeleteMapping("v1/adress/delete")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'MASTER')")
    public ResponseEntity<?> deleteAddress(@Valid @RequestBody RequestAddressDto requestAddressDto) {
        addressService.deleteAddressService();
        return null;
    }

}
