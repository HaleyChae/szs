package com.szs.service.refund;

import com.szs.dto.ApiResponseDto;
import com.szs.dto.RefundResponseDto;
import com.szs.entity.ScrapData;
import com.szs.exception.ApiException;
import com.szs.exception.ErrorEnum;
import com.szs.repository.ScrapDataRepository;
import com.szs.service.refund.factory.RefundCalculatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final ScrapDataRepository scrapDataRepository;
    private final RefundCalculatorFactory refundCalculatorFactory;

    public RefundResponseDto refund(String userId) {
        List<ScrapData> scrapDataList = scrapDataRepository.getByUserId(userId);

        if(scrapDataList.size() == 0) {
            throw new ApiException(ErrorEnum.MISSING_DATA_FOR_CALCULATION);
        }

        Map<String, Double> summary = scrapDataList.stream()
                .collect(Collectors.groupingBy(
                        ScrapData:: getDataType,
                        Collectors.summingDouble(t -> convertToDouble(t.getAmount()))
                ));

        RefundCalculator calculator = refundCalculatorFactory.getRefundCalculator();
        long finalTaxAmount = calculator.getFinalTaxAmount(summary);

        DecimalFormat formatter = new DecimalFormat("#,###");

        return RefundResponseDto.builder()
                .result(true)
                .message("결정세액 조회에 성공했습니다.")
                .finalTaxAmount(formatter.format(finalTaxAmount))
                .build();
    }

    private double convertToDouble(String str) {
        try {
            return Double.parseDouble(str.trim().replace(",", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
