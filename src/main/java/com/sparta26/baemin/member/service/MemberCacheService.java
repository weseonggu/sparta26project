package com.sparta26.baemin.member.service;

import com.sparta26.baemin.dto.member.RequestSignUpDto;
import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import com.sparta26.baemin.exception.exceptionsdefined.AlreadyDeletedException;
import com.sparta26.baemin.exception.exceptionsdefined.LoginFailException;
import com.sparta26.baemin.exception.exceptionsdefined.NoAccessToOtherPeopleData;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.member.client.MemberClientImpl;
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
    private  final MemberClientImpl memberClient;

    /**
     * 사용자 데이터 조회 후 캐싱
     * @param email 단일 계정 이메일 값
     * @return  ResponseMemberInfoDto
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "memberCache", key = "args[0]")
    public ResponseMemberInfoDto getMemberInfo(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new LoginFailException("사용자가 없습니다."));

        return new ResponseMemberInfoDto(member);
    }

    // 캐싱된 데이터를 날리고 DB에서 find한 정보를 수정새거 저장하는 방법을 써야한다.

    /**
     * 사용자일 경우 자기만 매니저는 사용자만  마스터는 매니저와 자기 자신만 이름과 닉네임만 변경 가능
     * @param requestSignUpDto 가입정보
     * @param userDetails 로그인한 정보
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "memberCache", key = "#userDetails.getEmail()")})
    public void updateMemberInfo(RequestSignUpDto requestSignUpDto, CustomUserDetails userDetails) {
        String role = userDetails.getRole();
        UserRole userRole = UserRole.fromString(role);
        switch (userRole){
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
                if(role.equals(UserRole.ROLE_MASTER.name()) || role.equals(UserRole.ROLE_MANAGER.name())){
                    chageInfoData(requestSignUpDto, userDetails);
                }else{
                    throw new IllegalArgumentException("타 매니저나 마스터 권한의 정보를 변경할 수 없습니다.");
                }
                break;
            case ROLE_MASTER:
                // 마스터는 매니저와 자기만 변경 가능
                if(role.equals(UserRole.ROLE_CUSTOMER.name())){
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
     * 사용자 삭제 일반 사용자는 자신만 매니저는 일반 사용자와 자신만 마스터는 일반 매니저와 자기 자신만
     * @param email 이메일
     * @param userDetails 로그인 정보
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "memberCache", key = "#email")})
    public void deleteMember(String email, CustomUserDetails userDetails) {
        UserRole role = UserRole.fromString(userDetails.getRole());
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 사용자 입니다."));
        if(!member.isPublic()){
            throw new AlreadyDeletedException("삭제된 데이터 입니다.");
        }
        switch (role){
            case ROLE_CUSTOMER:// 일반 사용자 자기 자신만 삭제 가능
                if(member.getEmail().equals(userDetails.getEmail())){
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                }
                else{
                    throw new NoAccessToOtherPeopleData("타인의 정보를 삭제할 수 없습니다.");
                }
                break;
            case ROLE_OWNER:// 가게사장 자기 자신과 가게 정보 삭제
                if(member.getEmail().equals(userDetails.getEmail())){
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                    // 추가적으로 상점까지 delete하기
                    memberClient.deleteStoreinfo(userDetails.getEmail(), userDetails.getRole(), userDetails.getId(), member);
                }
                else{
                    throw new NoAccessToOtherPeopleData("타인의 정보를 삭제할 수 없습니다.");
                }
                break;
            case ROLE_MANAGER: // 매니저 자기 자신과 일반 사용자, 가게사장 삭제 가능
                if(member.getEmail().equals(userDetails.getEmail())){
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                }
                else if(member.getRole().equals(UserRole.ROLE_CUSTOMER)){
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                }
                else if(member.getRole().equals(UserRole.ROLE_OWNER)){
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                    // 추가적으로 상점까지 delete하기
                    memberClient.deleteStoreinfo(userDetails.getEmail(), userDetails.getRole(), userDetails.getId(), member);
                }
                else{
                    throw new NoAccessToOtherPeopleData("타인의 정보를 삭제할 수 없습니다.");
                }
                break;
            case ROLE_MASTER:// 자기 자신과 매니저 삭제 가능
                if(member.getEmail().equals(userDetails.getEmail())){
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                }
                else if(member.getRole().equals(UserRole.ROLE_MANAGER)){
                    member.delete(userDetails.getEmail());
                    memberRepository.save(member);
                }
                else{
                    throw new NoAccessToOtherPeopleData("타인의 정보를 삭제할 수 없습니다.");
                }
                break;
            default:
                throw new IllegalArgumentException( "이상한 권한입니다.");
        }
    }


    /**
     * 사용자 정보 변경
     * @param requestSignUpDto 요청 정보
     * @param userDetails 로그인 정보
     * @return 맴버 정보 바꿔서 리턴
     */
    private Member chageInfoData(RequestSignUpDto requestSignUpDto, CustomUserDetails userDetails) throws  IllegalArgumentException{
        Member member = memberRepository.findByEmail(requestSignUpDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("없는 사용자입니다."));
        member.updateBy(requestSignUpDto.getUsername(), requestSignUpDto.getNickname(), userDetails.getEmail());
        return memberRepository.save(member);
    }
}
