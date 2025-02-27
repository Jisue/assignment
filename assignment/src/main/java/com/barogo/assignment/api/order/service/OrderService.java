package com.barogo.assignment.api.order.service;

import com.barogo.assignment.api.global.exception.CommonException;
import com.barogo.assignment.api.order.domain.entity.Order;
import com.barogo.assignment.api.order.domain.enums.DeliveryStatus;
import com.barogo.assignment.api.order.dto.request.OrderUpdateRequest;
import com.barogo.assignment.api.order.dto.response.OrderListResponse;
import com.barogo.assignment.api.order.dto.response.OrderResponse;
import com.barogo.assignment.api.order.repository.OrderRepository;
import com.barogo.assignment.api.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderListResponse getOrderList(LocalDate startDate, LocalDate endDate) {
        String userId = AuthenticationUtils.currentUserId().orElseThrow(() ->
                new CommonException("고객을 찾을 수 없습니다.", HttpStatus.FORBIDDEN)
        );

        // 날짜 확인
        if (!validateOrderListDate(startDate, endDate)) {
            log.warn("배달 주문 조회 날짜 검증 실패");
        }

        List<Order> orderList = orderRepository.findAllByUserId(userId, startDate.atStartOfDay(), endDate.atStartOfDay());
        List<OrderResponse> orderResponseList = orderList.stream().map(this::toOrderResponse).toList();

        return OrderListResponse.builder()
                .totalCount(orderResponseList.size())
                .orderList(orderResponseList).build();

    }

    @Transactional
    public void updateOrder(OrderUpdateRequest orderUpdateRequest) {
        Order order = orderRepository.findByOrderId(orderUpdateRequest.getOrderId()).orElseThrow(() ->
                new CommonException("주문을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST)
        );

        if (order.getDeliveryStatus() != DeliveryStatus.PENDING) {
            throw new CommonException("배송 시작 단계부터는 수정이 불가능합니다.", HttpStatus.BAD_REQUEST);
        }

        order.setAddress(orderUpdateRequest.getAddress());
        order.setUpdateDate(LocalDateTime.now());
    }

    private OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userName(order.getUser().getName())
                .receiverName(order.getReceiver())
                .paymentType(order.getPaymentType().getCode())
                .orderAmount(order.getOrderAmount())
                .paymentAmount(order.getPaymentAmount())
                .address(order.getAddress())
                .deliveryStatus(order.getDeliveryStatus().getCode())
                .createDate(order.getCreateDate())
                .build();
    }

    private boolean validateOrderListDate(LocalDate startDate, LocalDate endDate) {

        // startDate가 endDate보다 이전인지 확인
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
        }

        // 최대 3일까지 조회 가능
        if (endDate.isAfter(startDate.plusDays(3))) {
            throw new IllegalArgumentException("조회 가능한 기간은 최대 3일입니다.");
        }

        return true;
    }
}
