package com.sparta26.baemin.address.client;

import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import com.sparta26.baemin.member.service.MemberCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressClientImpl implements AddressClient{

    private final MemberCacheService memberCacheService;
    @Override
    public ResponseMemberInfoDto getMemberInfo(String email) {

        return memberCacheService.getMemberInfo(email);
    }
}
