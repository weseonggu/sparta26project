package com.sparta26.baemin.address.client;

import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;

public interface AddressClient {
    public ResponseMemberInfoDto getMemberInfo(String email);
}
