package com.szs.service.refund.v1;

import com.szs.service.refund.RefundCalculator;

import java.util.Map;

public class RefundCalculatorV1 implements RefundCalculator {

    /**
     * V1 결정세액 계산식
     * 과세표준 = 종합소득금액 - 소득공제
     * 산출세액 = 과세표준 * 기본세율
     * 결정세액 = 산출세액 - 세액공제
     *
     * @param summary 구분 별 합산 데이터 (Map.get("구분") == 합산금액)
     * @return 결정세액
     */
    @Override
    public long getFinalTaxAmount(Map<String, Double> summary) {
        long income = Math.round(summary.getOrDefault("종합소득", 0.0));
        long taxDeduction = Math.round(summary.getOrDefault("세액공제", 0.0));
        long deduction = Math.round(summary.getOrDefault("국민연금", 0.0)
                + summary.getOrDefault("신용카드", 0.0));
        return calculateTaxAmount(income - deduction) - taxDeduction;
    }


    /**
     * 산출세액 = 과세표준 * 기본세율
     * 1,400만원 이하 -> 과세표준의 6%
     * 1,400만원 초과~5,000만원 이하 -> 84만원 + (1,400만원 초과금액의 15%)
     * 5,000만원 초과~8,800만원 이하 -> 624만원 + (5,000만원 초과금액의 24%)
     * 8,800만원 초과~1억5천만원 이하 -> 1,536만원 + (8,800만원 초과금액의 35%)
     * 1억 5,000만원 초과~3억원 이하 -> 3,706만원 + (1억5천만원 초과금액의 38%)
     * 3억원 초과~5억원 이하 -> 9,406만원 + (3억원 초과금액의 40%)
     * 5억원 초과~10억원 이하 -> 17,406만원 + (5억원 초과금액의 42%)
     * 10억원 초과 -> 38,406만원 + (10억원 초과금액의 45%)
     *
     * @param taxableIncome 과세표준 (= 종합소득금액 - 소득공제)
     * @return 산출세액
     */
    private long calculateTaxAmount(long taxableIncome) {
        long taxAmount = 0;
        // 구간
        int[] range     = {0, 14000000  , 50000000  , 88000000  , 150000000 , 300000000 , 500000000 , 1000000000};
        // 기본 세금
        int[] baseTax   = {0, 840000    , 6240000   , 15360000  , 37060000  , 94060000  , 174060000 , 384060000};
        // 세율
        int[] taxRate   = {6, 15        , 24        , 35        , 38        , 40        , 42        , 45};

        int rangeIdx = range.length - 1;

        for(int i=1; i<range.length; i++) {
            if(taxableIncome > range[i]) continue;

            rangeIdx = i-1;
            break;
        }

        return baseTax[rangeIdx] + Math.round((double)(taxableIncome - range[rangeIdx]) * taxRate[rangeIdx] / 100.0);
    }
}
