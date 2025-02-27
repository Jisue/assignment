package com.barogo.assignment.api.order.repository;

import com.barogo.assignment.api.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("select o from Order o join fetch o.user " +
            "where o.user.userId= :userId " +
            "and o.createDate >= :startDate " +
            "and o.createDate <= :endDate " +
            "order by o.createDate desc")
    List<Order> findAllByUserId(String userId, LocalDateTime startDate, LocalDateTime endDate);

    Optional<Order> findByOrderId(String orderId);

}
