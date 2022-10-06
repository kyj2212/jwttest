package com.ll.exam.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthTests {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("POST /member/login 은 로그인 처리 URL 이다.")
    void t1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/member/login").with(csrf())
                                .content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))

                )
                .andDo(print());
    }
    @Test
    @DisplayName("POST /member/login 으로 Authentication 응답 header를 넘겨줄 수 있다.")
    void t2() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/member/login").with(csrf())
                                .content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))

                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());

        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String authentication = mockHttpServletResponse.getHeader("Authentication");
        System.out.println("Authentication response header : "+authentication);
        assertThat(authentication).isNotEmpty();
    }

    @Test
    @DisplayName("POST /member/login 으로 올바르지 않은 username과 password 데이터를 넘기면 400")
    void t3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/member/login")
                                .content("""
                                        {
                                            "username": "",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

        mvc
                .perform(
                        post("/member/login")
                                .content("""
                                        {
                                            "username": "user1",
                                            "password": ""
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());
    }
    @Test
    @DisplayName("POST /member/login 시 access token 이 헤더로 입력된다.")
    void t4() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/member/login")
                                .content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());

        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String authentication = mockHttpServletResponse.getHeader("Authentication");
        System.out.println("Authentication response header : "+authentication);
        assertThat(authentication).isNotEmpty();

    }

    @Test
    @DisplayName("로그인 후 얻은 JWT 토큰으로 현재 로그인 한 회원의 정보를 얻을 수 있다.")
    void t5() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/member/login")
                                .content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }l
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());

        MvcResult mvcResult = resultActions.andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        String accessToken = response.getHeader("Authentication");

        resultActions = mvc
                .perform(
                        get("/member/me")
                                .header("Authorization", "Bearer " + accessToken)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());

        // MemberController me 메서드에서는 @AuthenticationPrincipal MemberContext memberContext 를 사용해서 현재 로그인 한 회원의 정보를 얻어야 한다.

        // 추가
        // /member/me 에 응답 본문
        /*
          {
            "resultCode": "S-1",
            "msg": "성공",
            "data": {
              "id": 1,
              "createData": "날짜",
              "modifyData": "날짜",
              "username": "user1",
              "email": "user1@test.com"
            }
            "success": true,
            "fail": false
          }
       */
    }

}