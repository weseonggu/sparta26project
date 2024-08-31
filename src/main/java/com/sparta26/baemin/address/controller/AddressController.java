package com.sparta26.baemin.address.controller;

import com.sparta26.baemin.address.service.AddressService;
import com.sparta26.baemin.dto.address.RequestAddressDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addAddress(@Valid @RequestBody RequestAddressDto requestAddressDto) {
        return null;
    }

    /**
     * 주소 조회
     * @param member_id
     * @return
     */
    @GetMapping("v1/address/{member_id}")
    public ResponseEntity<?> getAddress(@PathVariable("member_id") int member_id) {
        return null;
    }

    /**
     * 주소 정보 변경
     * @param requestAddressDto
     * @return
     */
    @PutMapping("v1/adress/update")
    public ResponseEntity<?> updateAddress(@Valid @RequestBody RequestAddressDto requestAddressDto) {
        return null;
    }

    /**
     * 주소 데이터 비공개 처리
     * @param requestAddressDto
     * @return
     */
    @DeleteMapping("v1/adress/delete")
    public ResponseEntity<?> deleteAddress(@Valid @RequestBody RequestAddressDto requestAddressDto) {
        return null;
    }

}
