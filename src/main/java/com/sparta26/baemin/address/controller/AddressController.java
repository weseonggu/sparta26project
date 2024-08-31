package com.sparta26.baemin.address.controller;

import com.sparta26.baemin.address.service.AddressService;
import com.sparta26.baemin.dto.address.RequestAddressDto;
import com.sparta26.baemin.dto.address.ResponseAddressDto;
import com.sparta26.baemin.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 주소 등록
     * @return
     */
    @PostMapping("v1/address")
    public ResponseEntity<?> addAddress(@Valid @RequestBody RequestAddressDto requestAddressDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResponseAddressDto dto = new ResponseAddressDto(addressService.createAdressInfo(requestAddressDto, userDetails));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * 주소 조회
     * @param member_id
     * @return
     */
    @GetMapping("v1/address/{member_id}")
    public ResponseEntity<?> getAddress(@PathVariable("member_id") int member_id,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        addressService.getAddress(member_id, userDetails);
        return null;
    }

    /**
     * 주소 정보 변경
     * @param requestAddressDto
     * @return
     */
    @PutMapping("v1/adress/update")
    public ResponseEntity<?> updateAddress(@Valid @RequestBody RequestAddressDto requestAddressDto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        addressService.updateAddress(userDetails, requestAddressDto);
        return null;
    }

    /**
     * 주소 데이터 비공개 처리
     * @param requestAddressDto
     * @return
     */
    @DeleteMapping("v1/adress/delete")
    public ResponseEntity<?> deleteAddress(@Valid @RequestBody RequestAddressDto requestAddressDto) {
        addressService.deleteAddressService();
        return null;
    }

}
