package com.szs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.dto.LoginRequestDto;
import com.szs.dto.SignupRequestDto;
import com.szs.exception.ErrorEnum;
import com.szs.service.member.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @BeforeAll
    void signupUser() {
        memberService.signUp(
                SignupRequestDto.builder()
                        .name("관우")
                        .regNo("681108-1582816")
                        .userId("firstUser")
                        .password("firstUser1234")
                        .build()
        );
    }

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void signup() throws Exception{
        SignupRequestDto signupMember = SignupRequestDto.builder()
                .name("동탁")
                .regNo("921108-1582816")
                .userId("example1")
                .password("example1234")
                .build();

        callPost("/szs/signup", signupMember)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)))
                .andExpect(jsonPath("$.message", is("동탁님의 회원가입이 완료되었습니다.")));
    }

    @Test
    void signupNotValid() throws Exception {
        SignupRequestDto signupMember = SignupRequestDto.builder()
                .name("동탁")
                .regNo("921108-1582816")
                .password("example1234")
                .build();

        callPost("/szs/signup", signupMember)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    void signupInvalidPattern() throws Exception {
        SignupRequestDto signupMember = SignupRequestDto.builder()
                .name("동탁")
                .regNo("921532-8582816")
                .userId("example1")
                .password("example1234")
                .build();

        callPost("/szs/signup", signupMember)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    void signupUserNotEligible() throws Exception{
        SignupRequestDto signupMember = SignupRequestDto.builder()
                .name("채은별")
                .regNo("921108-1582816")
                .userId("example1")
                .password("example1234")
                .build();

        callPost("/szs/signup", signupMember)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.message", is(ErrorEnum.USER_NOT_ELIGIBLE.getMessage())));
    }

    @Test
    void signupAlreadyRegistered() throws Exception{
        SignupRequestDto signupMember = SignupRequestDto.builder()
                .name("관우")
                .regNo("681108-1582816")
                .userId("example1")
                .password("example1234")
                .build();

        callPost("/szs/signup", signupMember)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.message", is(ErrorEnum.ALREADY_REGISTERED.getMessage())));
    }

    @Test
    void signupDuplicateUserId() throws Exception{
        SignupRequestDto signupMember = SignupRequestDto.builder()
                .name("동탁")
                .regNo("921108-1582816")
                .userId("firstUser")
                .password("firstUser1234")
                .build();

        callPost("/szs/signup", signupMember)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.message", is(ErrorEnum.DUPLICATE_USERID.getMessage())));
    }

    @Test
    void login() throws Exception {
        LoginRequestDto loginMember = LoginRequestDto.builder()
                .userId("firstUser")
                .password("firstUser1234")
                .build();

        callPost("/szs/login",loginMember)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void loginNotValid() throws Exception {
        LoginRequestDto loginMember = LoginRequestDto.builder()
                .userId("firstUser")
                .build();

        callPost("/szs/login",loginMember)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    void loginInvalid() throws Exception {
        LoginRequestDto loginMember = LoginRequestDto.builder()
                .userId("firstUser")
                .password("wrongPassword")
                .build();

        callPost("/szs/login",loginMember)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.message", is(ErrorEnum.INVALID_ACCOUNT.getMessage())));
    }

    private ResultActions callPost(String url, Object obj) throws Exception{
        return mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(obj)));
    }
}