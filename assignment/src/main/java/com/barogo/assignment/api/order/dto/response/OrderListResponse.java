package com.barogo.assignment.api.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderListResponse {
    int totalCount;
    List<OrderResponse> orderList;
}
