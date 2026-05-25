package com.oms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class DashboardStats {
    private Long totalOrders;
    private Long totalCustomers;
    private BigDecimal averageOrderAmount;
    private Map<String, Long> ordersByStatus;
}
