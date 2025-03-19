package com.szs.controller;

import com.szs.dto.RefundResponseDto;
import com.szs.service.refund.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name="3. Refund", description = "세액조회")
public class RefundController {
    private final RefundService refundService;

    @Operation(summary = "결정세액 조회")
    @GetMapping("/szs/refund")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<RefundResponseDto> refund(@AuthenticationPrincipal String userId){
        return ResponseEntity.ok(refundService.refund(userId));
    }
}
