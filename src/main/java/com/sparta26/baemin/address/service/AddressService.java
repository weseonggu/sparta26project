package com.sparta26.baemin.address.service;

import com.sparta26.baemin.address.client.AddressClient;
import com.sparta26.baemin.address.entity.Address;
import com.sparta26.baemin.address.repository.AddressRepository;
import com.sparta26.baemin.dto.address.RequestAddressDto;
import com.sparta26.baemin.dto.address.ResponseAddressDto;
import com.sparta26.baemin.exception.exceptionsdefined.AlreadyDeletedException;
import com.sparta26.baemin.exception.exceptionsdefined.NoAccessToOtherPeopleData;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.member.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressClient addressClient;

    public Address createAdressInfo(RequestAddressDto requestAddressDto, CustomUserDetails userDetails) {
        Address address = new Address(requestAddressDto.getZipCode(),
                requestAddressDto.getRoadAddress(),
                requestAddressDto.getRoadAddressEnglish(),
                userDetails.getEmail(),
                userDetails.getId()
                );
        return addressRepository.save(address);

    }

    public Page<ResponseAddressDto>   getAddressService(Pageable page, Long memberId, CustomUserDetails userDetails) {
        Page<Address> addressPage=null;
        if(!addressClient.getMemberInfo(userDetails.getEmail()).isPublic()){
            throw new AlreadyDeletedException("삭제된 사용자입니다.");
        }
        // 매니저일 경우 어떤 사용자의 정보 조회가능
        if(userDetails.getRole().equals(UserRole.ROLE_MANAGER.name())){
            addressPage= addressRepository.findAllByMemberIdAndIsPublic(page, memberId, true);
        }
        // 일반 사용자일 경우 자기 주소만 조회 가능
        else if(Objects.equals(memberId, userDetails.getId())){
            addressPage= addressRepository.findAllByMemberIdAndIsPublic(page, userDetails.getId(), true);
        }else{
            throw new NoAccessToOtherPeopleData("타인의 주소는 볼 수없습니다.");
        }
        Page<ResponseAddressDto> addressDTOPage = addressPage.map(ResponseAddressDto:: new);
        return addressDTOPage;
    }

    public void updateAddressService(CustomUserDetails userDetails, RequestAddressDto requestAddressDto) {
    }

    /**
     * 주소 삭제 서비스
     * @param addressId
     * @param userDetails
     */
    public void deleteAddressService(UUID addressId, CustomUserDetails userDetails) {

        Address address = addressRepository.findById(addressId).get();
        if(address.isPublic() == false){
            throw new AlreadyDeletedException("이미 삭제된 데이터 입니다.");
        }
        address.delete(userDetails.getEmail());

        addressRepository.save(address);

    }
}
