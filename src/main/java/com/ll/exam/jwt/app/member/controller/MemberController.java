package com.ll.exam.jwt.app.member.controller;

import com.ll.exam.jwt.app.base.result.ResultResponse;
import com.ll.exam.jwt.app.member.dto.request.LoginDto;
import com.ll.exam.jwt.app.member.entity.Member;
import com.ll.exam.jwt.app.member.service.MemberService;
import com.ll.exam.jwt.app.security.entity.MemberContext;
import com.ll.exam.jwt.jwt.JwtProvider;
import com.ll.exam.jwt.util.Util;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginContext;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ResultResponse> login(@RequestBody LoginDto loginDto){


        if(loginDto.isNotValid()){
            return Util.spring.responseEntityOf(ResultResponse.of("F-1","잘못된 입력입니다." ,loginDto));
        }

        Optional<Member> oMember = memberService.findByUsername(loginDto.getUsername());
        if(!oMember.isPresent()){

            return Util.spring.responseEntityOf(ResultResponse.of("F-1","%s에 해당하는 아이디가 없습니다.".formatted(loginDto.getUsername()) ,loginDto.getUsername()));
        }
        Member member = oMember.get();

        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            return Util.spring.responseEntityOf(ResultResponse.of("F-1","비밀번호가 일치하지 않습니다." ,loginDto.getUsername()));
        }



        String accessToken = memberService.getAccessToken(member);
        HttpHeaders headers=Util.spring.httpHeadersOf("Authentication","Bearer "+accessToken);

        String body = "username : %s , password : %s".formatted(loginDto.getUsername(),loginDto.getPassword());
        return Util.spring.responseEntityOf(ResultResponse.of("S-1","로그인 성공" ,headers),headers);

    }

    @GetMapping("/me")
    public ResponseEntity<ResultResponse> me(@AuthenticationPrincipal MemberContext memberContext, Principal principal ){
        log.debug("principal : "+principal);
        return Util.spring.responseEntityOf(ResultResponse.of("S-1","성공" ,memberContext));
    }

}

