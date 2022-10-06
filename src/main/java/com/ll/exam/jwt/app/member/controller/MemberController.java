package com.ll.exam.jwt.app.member.controller;

import com.ll.exam.jwt.app.base.result.ResultResponse;
import com.ll.exam.jwt.app.member.entity.Member;
import com.ll.exam.jwt.app.member.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginContext;
import java.util.Optional;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ResultResponse> login(@RequestBody LoginDto loginDto){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication","Bearer (jwt accessToken)");

        if(loginDto.isNotValid()){
            return new ResponseEntity(ResultResponse.of("F-1","잘못된 입력입니다." ,loginDto),headers,HttpStatus.BAD_REQUEST);
        }

        Optional<Member> oMember = memberService.findByUsername(loginDto.getUsername());
        if(!oMember.isPresent()){
            return new ResponseEntity(ResultResponse.of("F-1","%s에 해당하는 아이디가 없습니다.".formatted(loginDto.getUsername()) ,loginDto.getUsername()),headers,HttpStatus.BAD_REQUEST);
        }
        Member member = oMember.get();

        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            return new ResponseEntity(ResultResponse.of("F-1","비밀번호가 일치하지 않습니다." ,loginDto.getUsername()),headers,HttpStatus.BAD_REQUEST);
        }

        String body = "username : %s , password : %s".formatted(loginDto.getUsername(),loginDto.getPassword());
        return new ResponseEntity(ResultResponse.of("S-1","로그인 성공" ,body),headers,HttpStatus.OK);

    }

    @Data
    public static class LoginDto {
        private String username;
        private String password;

        public boolean isNotValid(){
            return username == null || password == null || username.trim().length()==0 || password.trim().length()==0;
        }

    }
}

