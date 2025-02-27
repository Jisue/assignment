package com.barogo.assignment.api.order.domain.entity;

import com.barogo.assignment.api.order.domain.enums.DeliveryStatus;
import com.barogo.assignment.api.order.domain.enums.PaymentType;
import com.barogo.assignment.api.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "orders")
@Builder
public class Order {

    @Id
    @Column(name = "order_id", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '주문 고유 식별자'")
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_user_id", referencedColumnName = "user_id", nullable = false, columnDefinition = "VARCHAR(100) COMMENT '주문 고객 ID'")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = 4, nullable = false, columnDefinition = "VARCHAR(4) COMMENT '결제 유형'")
    private PaymentType paymentType;

    @Column(name = "receiver", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '받는 사람'")
    private String receiver;

    @Setter
    @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '도착지 주소'")
    private String address;

    @Column(name = "order_amount", nullable = false, columnDefinition = "BIGINT COMMENT '주문 금액'")
    private Long orderAmount;

    @Column(name = "payment_amount", columnDefinition = "BIGINT COMMENT '결제 금액'")
    private Long paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 10, nullable = false, columnDefinition = "VARCHAR(10) COMMENT '배달 상태'")
    private DeliveryStatus deliveryStatus;

    @Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMP(3) COMMENT '주문 날짜'")
    private LocalDateTime createDate;

    @Setter
    @Column(name = "update_date", columnDefinition = "TIMESTAMP(3) COMMENT '갱신 날짜'")
    private LocalDateTime updateDate;

}
