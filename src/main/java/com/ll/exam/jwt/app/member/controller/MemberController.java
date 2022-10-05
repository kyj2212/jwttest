package com.ll.exam.jwt.app.member.controller;

import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginContext;

@RestController
@RequestMapping("/member")
public class MemberController {
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody String body ){
        return body;
    }
}
