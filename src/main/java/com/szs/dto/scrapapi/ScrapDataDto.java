package com.szs.dto.scrapapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ScrapDataDto {
    @JsonProperty("이름")
    private String name;

    @JsonProperty("종합소득금액")
    private String income;

    @JsonProperty("소득공제")
    private Deduction deduction;

    // 공제
    @Data
    public static class Deduction {

        @JsonProperty("국민연금")
        private List<SocialSecurityDeduction> nationalPension;

        @JsonProperty("신용카드소득공제")
        private CreditCardDeduction creditCardDeduction;

        @JsonProperty("세액공제")
        private String taxDeduction;

        // 국민연금 소득공제
        @Data
        public static class SocialSecurityDeduction {

            @JsonProperty("월")
            private String month;

            @JsonProperty("공제액")
            private String deductionAmount;
        }

        // 신용카드 소득공제
        @Data
        public static class CreditCardDeduction {

            @JsonProperty("year")
            private String year;

            @JsonProperty("month")
            private List<Map<String, String>> monthDeductions;
        }
    }
}
