package com.oms.repository;

import com.oms.model.DeliveryStatus;
import com.oms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByStatus(DeliveryStatus status);
    List<Order> findByCustomerId(UUID customerId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(DeliveryStatus status);

    @Query("SELECT COALESCE(AVG(o.totalAmount), 0) FROM Order o")
    BigDecimal averageOrderAmount();
}
