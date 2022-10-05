package com.ll.exam.jwt.app.member.controller;

import com.ll.exam.jwt.app.member.entity.Member;
import com.ll.exam.jwt.app.member.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginContext;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private MemberService memberService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){

        if(loginDto.isNotValid()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Member member = memberService.findByUsername(loginDto.getUsername());

        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication","Bearer (jwt accessToken)");

        String body = "username : %s , password : %s".formatted(loginDto.getUsername(),loginDto.getPassword());
        return new ResponseEntity<>(body, headers ,HttpStatus.OK);
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

