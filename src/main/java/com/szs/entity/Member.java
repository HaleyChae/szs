package com.szs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long seq;

    @Column(nullable = false)
    @Schema(description = "이름")
    private String name;

    @Column(nullable = false)
    @Schema(description = "주민등록번호")
    private String regNo;

    @Column
    @Schema(description = "아이디")
    private String userId;

    @Column
    @Schema(description = "비밀번호")
    private String password;
}
