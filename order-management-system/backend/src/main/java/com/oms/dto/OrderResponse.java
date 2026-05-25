package com.oms.dto;

import com.oms.model.DeliveryStatus;
import com.oms.model.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class OrderResponse {
    private UUID id;
    private UUID customerId;
    private String customerName;
    private DeliveryStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderResponse from(Order o) {
        OrderResponse r = new OrderResponse();
        r.id = o.getId();
        r.customerId = o.getCustomer().getId();
        r.customerName = o.getCustomer().getName();
        r.status = o.getStatus();
        r.totalAmount = o.getTotalAmount();
        r.items = o.getItems().stream().map(OrderItemResponse::from).collect(Collectors.toList());
        r.createdAt = o.getCreatedAt();
        r.updatedAt = o.getUpdatedAt();
        return r;
    }
}
