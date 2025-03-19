package com.szs.service.scrap;

import com.szs.dto.scrapapi.ScrapApiRequestDto;
import com.szs.dto.ApiResponseDto;
import com.szs.entity.Member;
import com.szs.entity.ScrapData;
import com.szs.exception.ApiException;
import com.szs.exception.ErrorEnum;
import com.szs.repository.MemberRepository;
import com.szs.repository.ScrapDataRepository;
import com.szs.security.util.EncryptUtil;
import com.szs.service.scrap.factory.ScrapDataMapperFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {
    @Value("${scrap-api.url}")
    private String apiUrl;
    @Value("${scrap-api.key-field}")
    private String keyField;
    @Value("${scrap-api.key}")
    private String apiKey;

    private final MemberRepository memberRepository;
    private final ScrapDataRepository scrapDataRepository;
    private final EncryptUtil encryptUtil;
    private final ScrapDataMapperFactory scrapDataMapperFactory;

    public ApiResponseDto scrap(String userId) {
        Member member = memberRepository
                .findByUserId(userId)
                .orElseThrow(()-> new ApiException(ErrorEnum.UNKNOWN_USER));

        RestClient restClient = RestClient.create();

        String scrapResponse = restClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header(keyField, apiKey)
                .body(
                        ScrapApiRequestDto.builder()
                                .name(member.getName())
                                .regNo(encryptUtil.decrypt(member.getRegNo()))
                                .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, (rq, rs) -> {
                    throw new ApiException(ErrorEnum.SCRAP_API_FAILED);
                })
                .body(String.class);


        ScrapDataMapper mapper = scrapDataMapperFactory.getScrapDataMapper();
        List<ScrapData> scrapDataList = mapper.getScrapDataList(scrapResponse, userId, LocalDateTime.now());

        // 이전 스크랩 데이터 삭제
        scrapDataRepository.deleteByUserId(userId);

        // 새 스크랩 데이터 추가
        scrapDataRepository.saveAll(scrapDataList);

        return ApiResponseDto.builder().result(true)
                .message(member.getName() + "님 " + scrapDataList.size() + "건 스크랩 완료입니다.")
                .build();
    }
}
