package com.szs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "Refund Response")
public class RefundResponseDto extends ApiResponseDto{
    @Schema(description = "결정세액")
    @JsonProperty("결정세액")
    private String finalTaxAmount;
}
