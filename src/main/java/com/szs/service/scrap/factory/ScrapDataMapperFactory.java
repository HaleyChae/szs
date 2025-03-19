package com.szs.service.scrap.factory;

import com.szs.exception.ApiException;
import com.szs.exception.ErrorEnum;
import com.szs.service.scrap.ScrapDataMapper;
import com.szs.service.scrap.v1.ScrapDataMapperV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScrapDataMapperFactory {
    @Value("${scrap-api.version:v1}")
    private String apiVersion;

    public ScrapDataMapper getScrapDataMapper() {
        if ("v1".equals(apiVersion)) {
            return new ScrapDataMapperV1();
        }
        throw new ApiException(ErrorEnum.UNSUPPORTED_VERSION);
    }
}
