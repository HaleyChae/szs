package com.szs.controller;

import com.szs.dto.SignupRequestDto;
import com.szs.entity.Member;
import com.szs.exception.ErrorEnum;
import com.szs.repository.MemberRepository;
import com.szs.security.JwtAuthenticationFilter;
import com.szs.security.JwtTokenProvider;
import com.szs.security.config.SecurityConfig;
import com.szs.service.member.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScrapControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(jwtAuthenticationFilter)
                .build();
    }

    @Test
    void scrap() throws Exception {
        memberService.signUp(
                SignupRequestDto.builder()
                        .name("관우")
                        .regNo("681108-1582816")
                        .userId("firstUser")
                        .password("firstUser1234")
                        .build()
        );

        String token = jwtTokenProvider.generateAccessToken("firstUser");

        mvc.perform(post("/szs/scrap" ).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    void scrapTokenExpire() throws Exception {
        String expireToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJleGFtcGxlMSIsImlhdCI6MTc0MjM2ODYzNSwiZXhwIjoxNzQyMzY4Njk1fQ.bJPT5bnwBP8wL-a81akLkjl4unvdzVoByNqwCoevLa8";
        mvc.perform(post("/szs/scrap").header(HttpHeaders.AUTHORIZATION, "Bearer " + expireToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.message", is(ErrorEnum.TOKEN_EXPIRE.getMessage())));
    }

    @Test
    void scrapNoToken() throws Exception {
        String invalidToken = "fakeToken";
        mvc.perform(post("/szs/scrap").header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.message", is(ErrorEnum.NO_TOKEN.getMessage())));
    }

    @Test
    void scrapApiResultFail() throws Exception {
        Member fakeMember = Member.builder()
                .name("채은별")
                .regNo("sfQX2fGgMqKy8qHaaVG+nA==")
                .userId("fakeMember")
                .build();
        memberRepository.save(fakeMember);

        String fakeMemberToken = jwtTokenProvider.generateAccessToken("fakeMember");

        mvc.perform(post("/szs/scrap").header(HttpHeaders.AUTHORIZATION, "Bearer " + fakeMemberToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(false)))
                .andExpect(jsonPath("$.message", is(ErrorEnum.SCRAP_API_RESULT_FAILED.getMessage())));
    }
}