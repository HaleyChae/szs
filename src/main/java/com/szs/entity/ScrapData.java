package com.szs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScrapData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long seq;

    @Column(nullable = false)
    @Schema(description = "사용자 ID")
    private String userId;

    @Column
    @Schema(description = "이름")
    private String name;

    @Column(nullable = false)
    @Schema(description = "구분")
    private String dataType;

    @Column
    @Schema(description = "년 yyyy")
    private String dataYear;

    @Column
    @Schema(description = "월 mm")
    private String dataMonth;

    @Column
    @Schema(description = "금액")
    private String amount;

    @Column(nullable = false)
    @Schema(description = "스크랩 시간")
    private LocalDateTime scrapeTime;
}
