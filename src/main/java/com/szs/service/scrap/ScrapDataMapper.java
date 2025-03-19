package com.szs.service.scrap;

import com.szs.entity.ScrapData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ScrapDataMapper {

    /**
     * 저장 할 scrap 데이터 원본 리스트 추출
     *
     * @param responseJson API Response Body
     * @param userId 로그인 사용자 ID
     * @param scrapTime 스크랩 한 시간
     * @return Scrap Data Entity List
     */
    List<ScrapData> getScrapDataList(String responseJson, String userId, LocalDateTime scrapTime) ;
}
