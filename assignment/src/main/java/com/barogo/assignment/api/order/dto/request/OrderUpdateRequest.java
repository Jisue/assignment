package com.barogo.assignment.api.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderUpdateRequest {
    @NotBlank(message = "orderId는 필수입니다.")
    private String orderId;
    private String address;
}
