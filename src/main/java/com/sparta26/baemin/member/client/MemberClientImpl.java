package com.sparta26.baemin.member.client;

import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberClientImpl implements MemberClient{

    private final StoreService storeService;

    @Override
    public void deleteStoreinfo(String email, String role, Long memberId, Member member) {
        Store store = member.getStore();
        if(store==null) return;
        storeService.deleteStore(store.getId().toString(), email, role, memberId);
    }
}
