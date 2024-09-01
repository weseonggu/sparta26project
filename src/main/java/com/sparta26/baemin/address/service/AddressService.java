package com.sparta26.baemin.address.service;

import com.sparta26.baemin.address.entity.Address;
import com.sparta26.baemin.address.repository.AddressRepository;
import com.sparta26.baemin.dto.address.RequestAddressDto;
import com.sparta26.baemin.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address createAdressInfo(RequestAddressDto requestAddressDto, CustomUserDetails userDetails) {
        Address address = new Address(requestAddressDto.getZipCode(),
                requestAddressDto.getRoadAddress(),
                requestAddressDto.getRoadAddressEnglish(),
                userDetails.getEmail(),
                userDetails.getId()
                );
        return addressRepository.save(address);

    }

    public void getAddress(int memberId, CustomUserDetails userDetails) {
    }

    public void updateAddress(CustomUserDetails userDetails, RequestAddressDto requestAddressDto) {
    }

    public void deleteAddressService() {
    }
}
