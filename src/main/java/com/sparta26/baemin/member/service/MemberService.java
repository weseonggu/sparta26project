package com.sparta26.baemin.member.service;

import com.sparta26.baemin.dto.member.RequestLogInDto;
import com.sparta26.baemin.dto.member.RequestSignUpDto;
import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import com.sparta26.baemin.exception.exceptionsdefined.LoginFailException;
import com.sparta26.baemin.exception.exceptionsdefined.MemberNotFoundException;
import com.sparta26.baemin.jwt.JWTUtil;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private  final MemberCacheService memberCacheService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JWTUtil jwtUtil;

    @Transactional
    public Member createMember(RequestSignUpDto member){
        UserRole role = UserRole.fromString(member.getRoleCode());
        Member user;
        if(memberRepository.existsByEmail(member.getEmail())){
            throw new DuplicateKeyException("이미 가입한 이메일 입니다.");
        }
        user = Member.builder()
                .email(member.getEmail())
                .password(passwordEncoder.encode(member.getPassword()))
                .username(member.getUsername())
                .nickname(member.getNickname())
                .role(role)
                .build();
        return memberRepository.save(user);

    }
    @Transactional(readOnly = true)
    public String attemptLogIn(RequestLogInDto member) {
        // DB에서 사용장 정보 가져 오기
        ResponseMemberInfoDto db_member = memberCacheService.getMemberInfo(member.getEmail());
        // 비번 비교 로직, 토큰 생성
        String token = null;
        if(passwordEncoder.matches(member.getPassword(), db_member.getPassword())){
            token = jwtUtil.createToken(db_member.getId(),db_member.getEmail(),db_member.getRole());
        }else{
            throw new LoginFailException("이메일이나 비밀번호가 틀렸습니다.");
        }
        return token;
    }
}
