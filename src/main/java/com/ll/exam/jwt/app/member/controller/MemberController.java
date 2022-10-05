package com.ll.exam.jwt.app.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginContext;

@RestController
@RequestMapping("/member")
public class MemberController {
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody String body, @RequestHeader HttpHeaders reqHeaders){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication",reqHeaders.getFirst("Authentication"));
        return new ResponseEntity<>(body, headers ,HttpStatus.OK);
    }
}
