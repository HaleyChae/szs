package com.szs.service.refund.factory;

import com.szs.exception.ApiException;
import com.szs.exception.ErrorEnum;
import com.szs.service.refund.RefundCalculator;
import com.szs.service.refund.v1.RefundCalculatorV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefundCalculatorFactory {
    @Value("${refund.version:v1}")
    private String refundVersion;

    public RefundCalculator getRefundCalculator() {
        if ("v1".equals(refundVersion)) {
            return new RefundCalculatorV1();
        }
        throw new ApiException(ErrorEnum.UNSUPPORTED_VERSION);
    }
}
