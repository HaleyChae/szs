package com.szs.service.scrap.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.dto.scrapapi.ScrapDataDto;
import com.szs.dto.scrapapi.ScrapApiResponseDto;
import com.szs.entity.ScrapData;
import com.szs.exception.ApiException;
import com.szs.exception.ErrorEnum;
import com.szs.service.scrap.ScrapDataMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScrapDataMapperV1 implements ScrapDataMapper {
    @Override
    public List<ScrapData> getScrapDataList(String responseJson, String userId, LocalDateTime scrapTime) {
        List<ScrapData> scrapDataList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        ScrapApiResponseDto response;

        try {
            response = objectMapper.readValue(responseJson, ScrapApiResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorEnum.SCRAP_API_RESPONSE_MAPPING_FAILED);
        }

        if(!"success".equals(response.getStatus())) {
            throw new ApiException(ErrorEnum.SCRAP_API_RESULT_FAILED);
        }

        ScrapDataDto scrapData = response.getScrapData();
        if(scrapData == null) {
            throw new ApiException(ErrorEnum.NO_DATA_TO_SCRAP);
        }

        String scrapName = scrapData.getName();

        // 종합소득 금액
        scrapDataList.add(
                ScrapData.builder()
                        .userId(userId)
                        .name(scrapName)
                        .dataType("종합소득")
                        .amount(scrapData.getIncome())
                        .scrapeTime(scrapTime)
                        .build()
        );

        ScrapDataDto.Deduction deduction = scrapData.getDeduction();
        if(deduction == null) return scrapDataList;

        // 세액공제 금액
        scrapDataList.add(
                ScrapData.builder()
                        .userId(userId)
                        .name(scrapName)
                        .dataType("세액공제")
                        .amount(deduction.getTaxDeduction())
                        .scrapeTime(scrapTime)
                        .build()
        );

        // 국민연금 소득공제 금액
        scrapDataList.addAll(
                deduction.getNationalPension().stream()
                        .map(t -> ScrapData.builder()
                                .userId(userId)
                                .name(scrapName)
                                .dataType("국민연금")
                                .dataYear(t.getMonth().split("-")[0])
                                .dataMonth(t.getMonth().split("-")[1])
                                .amount(t.getDeductionAmount())
                                .scrapeTime(scrapTime)
                                .build()
                        ).toList()
        );

        ScrapDataDto.Deduction.CreditCardDeduction creditCardDeduction = deduction.getCreditCardDeduction();
        if(creditCardDeduction == null) return scrapDataList;

        // 신용카드 소득공제 금액
        scrapDataList.addAll(
                creditCardDeduction.getMonthDeductions().stream()
                        .flatMap(map -> map.entrySet().stream())
                        .map(t -> ScrapData.builder()
                                .userId(userId)
                                .name(scrapName)
                                .dataType("신용카드")
                                .dataYear(creditCardDeduction.getYear())
                                .dataMonth(t.getKey())
                                .amount(t.getValue())
                                .scrapeTime(scrapTime)
                                .build()
                        ).toList()
        );

        return scrapDataList;
    }
}
