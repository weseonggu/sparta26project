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

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new LoginFailException("사용자가 없습니다."));

        ResponseMemberInfoDto m_info = new ResponseMemberInfoDto(member);
        return m_info;
    }

    // 캐싱된 데이터를 날리고 DB에서 find한 정보를 수정새거 저장하는 방법을 써야한다.

    /**
     * 사용자일 경우 자기만 매니저는 사용자만  마스터는 매니저와 자기 자신만 이름과 닉네임만 변경 가능
     * @param requestSignUpDto
     * @param userDetails
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "memberCache", key = "#userDetails.getEmail()")})
    public void updateMemberInfo(RequestSignUpDto requestSignUpDto, CustomUserDetails userDetails) {
        UserRole role = UserRole.fromString(userDetails.getRole());
        switch (role){
            case ROLE_CUSTOMER:
                // 사용자 자기 정보 변경가능
                if(requestSignUpDto.getEmail().equals(userDetails.getEmail())){
                    chageInfoData(requestSignUpDto, userDetails);
                }
                else{
                    throw new IllegalArgumentException("타인의 정보는 변경할 수 없습니다.");
                }
                break;
            case ROLE_MANAGER:
                // 매니저는 일반 사용자의 정보 변경이 가능
                if(role.equals(UserRole.ROLE_MASTER) || role.equals(UserRole.ROLE_MANAGER)){
                    chageInfoData(requestSignUpDto, userDetails);
                }else{
                    throw new IllegalArgumentException("타 매니저나 마스터 권한의 정보를 변경할 수 없습니다.");
                }
                break;
            case ROLE_MASTER:
                // 마스터는 매니저와 자기만 변경 가능
                if(role.equals(UserRole.ROLE_CUSTOMER)){
                    chageInfoData(requestSignUpDto, userDetails);
                }else{
                    throw new IllegalArgumentException("마스터는 일반 사용자의 정보를 변경할 수 없습니다.");
                }
                break;
            default:
                throw new IllegalArgumentException( "이상한 권한입니다.");
        }
    }

    /**
     * @param email
     * @param userDetails
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "memberCache", key = "#email")})
    public void deleteMember(String email, CustomUserDetails userDetails) {
        UserRole role = UserRole.fromString(userDetails.getRole());
        Member member =null;
        switch (role){
            case ROLE_CUSTOMER:
                // 사용자 자기 정보 변경가능
                if(email.equals(userDetails.getEmail())){
                    member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                }
                else{
                    throw new IllegalArgumentException("타인을 탈퇴 할 수 없습니다.");
                }
                break;
            case ROLE_MANAGER:
                member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));
                member.delete(userDetails.getEmail());
                memberRepository.save(member);
            case ROLE_MASTER:
                member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));
                member.delete(userDetails.getEmail());
                memberRepository.save(member);
            default:
                throw new IllegalArgumentException( "이상한 권한입니다.");
        }
    }

    /**
     * 사용자 정보 변경
     * @param requestSignUpDto
     * @param userDetails
     * @return
     */
    private Member chageInfoData(RequestSignUpDto requestSignUpDto, CustomUserDetails userDetails) throws  IllegalArgumentException{
        Member member = memberRepository.findByEmail(requestSignUpDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("없는 사용자입니다."));
        member.updateBy(requestSignUpDto.getUsername(), requestSignUpDto.getNickname(), userDetails.getEmail());
        return memberRepository.save(member);
    }
}
