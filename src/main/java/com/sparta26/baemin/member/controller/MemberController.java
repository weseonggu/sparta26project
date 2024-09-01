package com.sparta26.baemin.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sparta26.baemin.dto.member.*;
import com.sparta26.baemin.jwt.CustomUserDetails;
import com.sparta26.baemin.member.service.MemberCacheService;
import com.sparta26.baemin.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberCacheService memberCacheService;
    private final ObjectMapper objectMapper;

    /**
     * 회원 가입
     * @param member: 이름, 닉네임, 이메일, 비번, 권한(이상한 권한 입력시 가입 못함)
     * @return 가입정보
     */
    @PostMapping("/v1/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody RequestSignUpDto member) {
        memberService.createMember(member);
        return new ResponseEntity<>("회원가입에 성공했습니다.", HttpStatus.OK);
    }

    /**
     * 로그인 요청
     * @param member 이메일, 비밀번호
     * @return 로그인 정보
     */
    @PostMapping("/v1/logIn")
    public ResponseEntity<?> logIn(@Valid @RequestBody RequestLogInDto member) {
        TokenAndMemberInfoDto dto = memberService.attemptLogIn(member);
        // 토큰 헤더에 넣기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", dto.getToken());

        // 로그인 정보 필터
        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(
                "MemberInfoFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("id","email", "username","nickname", "role")
        );

        objectMapper.setFilterProvider(filters);
        return new ResponseEntity<>(dto.getMemberInfo(), headers, HttpStatus.OK);
    }

    /**
     * 사용자 정보 조회 사용자일 경우 본인만 가능하고 매니저일경우 타인의 정보 조회 가능
     * @param email  사용자 이메일
     * @return 로그인한 자기 정보
     */
    @GetMapping("/v1/members/myinfo/{email}")
    @PreAuthorize("(isAuthenticated() && principal.username == #email)")
    public ResponseEntity<?> getMember(@PathVariable("email") String email) {
        ResponseMemberInfoDto memberInfo = memberCacheService.getMemberInfo(email);
        // 수동 필터
        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(
                "MemberInfoFilter",
                SimpleBeanPropertyFilter.serializeAllExcept("password")
        );
        objectMapper.setFilterProvider(filters);
        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }

    /**
     * 페이징을 사용한 사용자 전체 조회 매니저, 마스터만 가능
     * @param pageable 페이징할 데이터
     * @return 매니저나 마스터가 사용자 정보 조회 여러 있기 때문에 페이징 데이터 리턴
     */
    @GetMapping("/v1/members/page")
    @PreAuthorize("isAuthenticated() && (hasAuthority('ROLE_MANAGER')|| hasAuthority('ROLE_MASTER'))")
    public Page<ResponseMemberInfoDto> getMember(@PageableDefault(size = 10) Pageable pageable, RequestSearchMemberDto request){
        Page<ResponseMemberInfoDto> page = memberService.memberInfoInPage(pageable, request);
        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(
                "MemberInfoFilter",
                SimpleBeanPropertyFilter.serializeAllExcept("password")
        );
        objectMapper.setFilterProvider(filters);
        return page;
    }

    /**
     * 개별 사용자 정보 수정 사용자의 경우 본인만 가능, 매니저의 경우는 타인도 가능
     * @param requestSignUpDto 바꿀데이터
     * @param userDetails 로그인한 사용자 정보
     * @return 변경여부
     */
    @PatchMapping("/v1/members/update")
    public ResponseEntity<String> updateMemberInfo(@RequestBody RequestSignUpDto requestSignUpDto, @AuthenticationPrincipal CustomUserDetails userDetails){
        memberCacheService.updateMemberInfo(requestSignUpDto, userDetails);
        return new ResponseEntity<>("변경했습니다.", HttpStatus.OK);
    }
    
    // 맴버 삭제: 삭제가 아닌 정보 공개 여부를 false로 변경

    /**
     * 사용자 삭제 일반 사용자는 자기만 매니저와 마스터는 타인까지 제거가 가능
     * @param email 이메일
     * @param userDetails 로그인 정보
     * @return 탈퇴 여부
     */
    @DeleteMapping("/v1/members/delete/{email}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'OWN', 'MASTER','OWNER')")
    public ResponseEntity<String> deleteMember(@PathVariable("email") String email, @AuthenticationPrincipal CustomUserDetails userDetails){
        memberCacheService.deleteMember(email, userDetails);
        return new ResponseEntity<>("탈퇴되었습니다.", HttpStatus.OK);
    }

}
