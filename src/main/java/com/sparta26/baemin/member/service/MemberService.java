package com.sparta26.baemin.member.service;

import com.sparta26.baemin.dto.member.RequestLogInDto;
import com.sparta26.baemin.dto.member.RequestSignUpDto;
import com.sparta26.baemin.jwt.JWTUtil;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JWTUtil jwtUtil;

    @Transactional
    public Member createMember(RequestSignUpDto member){
            UserRole role = null;
            if(member.getRoleCode().equals("sparta26")){
                role= UserRole.ROLE_CUSTOMER;
            }
            if(memberRepository.existsByEmail(member.getEmail())){
                throw new DuplicateKeyException("이미 가입한 이메일 입니다.");
            }
            Member user = Member.builder()
                    .email(member.getEmail())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .role(role)
                    .build();
            user.addCreatedBy(member.getUsername());
            return memberRepository.save(user);
    }

    public String attemptLogIn(RequestLogInDto member) {
        Member db_member = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Member not found with email: " + member.getEmail()));

        return jwtUtil.createToken(db_member.getId(),db_member.getEmail(),db_member.getRole());
    }
}
