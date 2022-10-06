package com.ll.exam.jwt.app.member.service;

import com.ll.exam.jwt.app.member.entity.Member;
import com.ll.exam.jwt.app.member.repository.MemberRepository;
import com.ll.exam.jwt.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public String getAccessToken(Member member) {
        return jwtProvider.generateAccessToken(member.getClaims(), 60 * 60 * 5);
    }
}