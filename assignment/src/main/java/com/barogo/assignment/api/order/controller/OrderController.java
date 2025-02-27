package com.barogo.assignment.api.order.controller;

import com.barogo.assignment.api.global.common.response.CommonResponse;
import com.barogo.assignment.api.order.dto.request.OrderUpdateRequest;
import com.barogo.assignment.api.order.dto.response.OrderListResponse;
import com.barogo.assignment.api.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping(value="/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<OrderListResponse>> getOrderList(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return CommonResponse.success(orderService.getOrderList(startDate, endDate));
    }

    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponse<Void>> updateOrderAddress(@RequestBody OrderUpdateRequest orderUpdateRequest) {
        orderService.updateOrder(orderUpdateRequest);
        return CommonResponse.success(null);
    }
}