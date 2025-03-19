package com.szs.service.refund;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RefundCalculator {

    /**
     * @param summary 구분 별 합산 데이터 (Map.get("구분") == 합산금액)
     * @return 결정세액
     */
    long getFinalTaxAmount(Map<String, Double> summary);
}
