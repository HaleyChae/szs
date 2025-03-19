package com.szs.dto.scrapapi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScrapApiRequestDto {
    private String name;
    private String regNo;
}
