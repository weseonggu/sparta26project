package com.sparta26.baemin.member.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sparta26.baemin.category.entity.Category;
import com.sparta26.baemin.dto.member.RequestLogInDto;
import com.sparta26.baemin.dto.member.RequestSignUpDto;
import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.service.MemberCacheService;
import com.sparta26.baemin.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberCacheService memberCacheService;

    @PostMapping("/v1/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody RequestSignUpDto member) {
        memberService.createMember(member);
        return new ResponseEntity<String>("회원가입에 성공했습니다.", HttpStatus.OK);
    }

    @PostMapping("/v1/logIn")
    public ResponseEntity<?> logIn(@Valid @RequestBody RequestLogInDto member) {
        String token = memberService.attemptLogIn(member);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return new ResponseEntity<String>("로그인 성공했습니다.", headers, HttpStatus.OK);
    }

    /**
     * 사용자 정보 조회 사용자일 경우 본인만 가능하고 매니저일경우 타인의 정보 조회 가능
     * @param email
     * @return
     */
    @GetMapping("/v1/members/{email}")
    @PreAuthorize("(isAuthenticated() && principal.getEmail() == #email) || principal.getEmail() == ROLE_MANAGER")
    public ResponseEntity<?> getMember(@PathVariable("email") String email) {
        ResponseMemberInfoDto memberInfo = memberCacheService.getMemberInfo(email);
        // 수동 필터
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(memberInfo);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","username", "email", "nickname");
        FilterProvider filters =  new SimpleFilterProvider().addFilter("MemberInfoFilter", filter);
        mappingJacksonValue.setFilters(filters);
        return new ResponseEntity<MappingJacksonValue> (mappingJacksonValue,HttpStatus.OK);
    }
}
