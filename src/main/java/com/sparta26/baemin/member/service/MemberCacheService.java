package com.sparta26.baemin.member.service;

import com.sparta26.baemin.dto.member.RequestSignUpDto;
import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import com.sparta26.baemin.exception.exceptionsdefined.LoginFailException;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberCacheService {
    private final MemberRepository memberRepository;

    /**
     * 사용자 데이터 조회 후 캐싱
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "memberCache", key = "args[0]")
    public ResponseMemberInfoDto getMemberInfo(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new LoginFailException("사용자가 없습니다."));

        ResponseMemberInfoDto m_info = new ResponseMemberInfoDto(member);
        return m_info;
    }

    // 캐싱된 데이터를 날리고 DB에서 find한 정보를 수정새거 저장하는 방법을 써야한다.
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "memberCache", key = "#userDetails.getEmail()")})
    public void updateMemberInfo(RequestSignUpDto requestSignUpDto, CustomUserDetails userDetails) {
        if (
                requestSignUpDto.getEmail().equals(userDetails.getEmail()) ||
                (userDetails.getRole().equals(UserRole.ROLE_MANAGER) || userDetails.getRole().equals(UserRole.ROLE_MASTER))
        ) {

            memberRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new LoginFailException("없는 사용자입니다."));
        }else{
            throw new IllegalArgumentException("타인의 정보 변경은 불가능합니다.");
        }
    }
}
