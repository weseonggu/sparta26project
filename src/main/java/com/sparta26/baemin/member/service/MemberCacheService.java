package com.sparta26.baemin.member.service;

import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import com.sparta26.baemin.exception.exceptionsdefined.LoginFailException;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberCacheService {
    private final MemberRepository memberRepository;
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "memberCache", key = "args[0]")
    public ResponseMemberInfoDto getMemberInfo(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new LoginFailException("사용자가 없습니다."));

        ResponseMemberInfoDto m_info = new ResponseMemberInfoDto(member);
        return m_info;
    }
}
