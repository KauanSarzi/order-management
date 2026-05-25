package com.oms.dto;

import com.oms.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemResponse {
    private UUID id;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public static OrderItemResponse from(OrderItem i) {
        OrderItemResponse r = new OrderItemResponse();
        r.id = i.getId();
        r.productName = i.getProductName();
        r.quantity = i.getQuantity();
        r.unitPrice = i.getUnitPrice();
        r.subtotal = i.getSubtotal();
        return r;
    }
}
