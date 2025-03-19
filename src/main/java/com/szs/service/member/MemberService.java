package com.szs.service.member;

import com.szs.dto.LoginRequestDto;
import com.szs.dto.ApiResponseDto;
import com.szs.dto.LoginResponseDto;
import com.szs.dto.SignupRequestDto;
import com.szs.entity.Member;
import com.szs.exception.ApiException;
import com.szs.exception.ErrorEnum;
import com.szs.repository.MemberRepository;
import com.szs.security.JwtTokenProvider;
import com.szs.security.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final EncryptUtil encryptUtil;

    private final JwtTokenProvider jwtTokenProvider;

    public ApiResponseDto signUp(SignupRequestDto signupMember) {
        signupMember.setRegNo(encryptUtil.encrypt(signupMember.getRegNo()));
        Member member = memberRepository
                .findByNameAndRegNo(signupMember.getName(), signupMember.getRegNo())
                .orElseThrow(()->new ApiException(ErrorEnum.USER_NOT_ELIGIBLE));

        if(member.getUserId() != null) {
            throw new ApiException(ErrorEnum.ALREADY_REGISTERED);
        }
        if(memberRepository.existsByUserId(signupMember.getUserId())) {
            throw new ApiException(ErrorEnum.DUPLICATE_USERID);
        }

        memberRepository.save(
                Member.builder()
                        .seq(member.getSeq())
                        .userId(signupMember.getUserId())
                        .password(passwordEncoder.encode(signupMember.getPassword()))
                        .name(signupMember.getName())
                        .regNo(signupMember.getRegNo())
                        .build()
        );

        return ApiResponseDto.builder()
                .result(true)
                .message(signupMember.getName() + "님의 회원가입이 완료되었습니다.")
                .build();
    }

    public LoginResponseDto login(LoginRequestDto loginMember) {
        Member member = memberRepository
                .findByUserId(loginMember.getUserId())
                .orElseThrow(()-> new ApiException(ErrorEnum.INVALID_ACCOUNT));

        if(!passwordEncoder.matches(loginMember.getPassword(), member.getPassword())) {
            throw new ApiException(ErrorEnum.INVALID_ACCOUNT);
        }

        return LoginResponseDto.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(loginMember.getUserId()))
                .message(member.getName() + "님 환영합니다.")
                .result(true)
                .build();
    }
}
