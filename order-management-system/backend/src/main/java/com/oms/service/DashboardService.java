package com.oms.service;

import com.oms.dto.DashboardStats;
import com.oms.model.DeliveryStatus;
import com.oms.repository.CustomerRepository;
import com.oms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalOrders(orderRepository.count());
        stats.setTotalCustomers(customerRepository.count());
        stats.setAverageOrderAmount(orderRepository.averageOrderAmount());

        Map<String, Long> byStatus = Arrays.stream(DeliveryStatus.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        s -> orderRepository.countByStatus(s)
                ));
        stats.setOrdersByStatus(byStatus);

        return stats;
    }
}
