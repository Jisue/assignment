package com.barogo.assignment.api.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponse {
    private String orderId;
    private String userName;
    private String receiverName;
    private String paymentType;
    private Long orderAmount;
    private Long paymentAmount;
    private String address;
    private String deliveryStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime createDate;
}
