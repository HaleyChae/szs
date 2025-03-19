package com.szs.dto.scrapapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScrapApiResponseDto {
    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private ScrapDataDto scrapData;

    @JsonProperty("errors")
    private ScrapFailRequestDto scrapFail;
}
