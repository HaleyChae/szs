package com.szs.controller;

import com.szs.dto.ApiResponseDto;
import com.szs.service.scrap.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name="2. Scrap", description = "스크랩")
public class ScrapController {
    private final ScrapService scrapService;

    @Operation(summary = "스크래핑")
    @PostMapping("/szs/scrap")
    public ResponseEntity<ApiResponseDto> scrap(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(scrapService.scrap(userId));
    }
}
