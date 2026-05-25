package com.oms.service;

import com.oms.dto.*;
import com.oms.model.DeliveryStatus;
import com.oms.model.Order;
import com.oms.model.OrderItem;
import com.oms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    public List<OrderResponse> findAll(DeliveryStatus status) {
        List<Order> orders = status != null
                ? orderRepository.findByStatus(status)
                : orderRepository.findAll();
        return orders.stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    public OrderResponse findById(UUID id) {
        return OrderResponse.from(getOrThrow(id));
    }

    @Transactional
    public OrderResponse create(OrderRequest req) {
        Order order = new Order();
        order.setCustomer(customerService.getOrThrow(req.getCustomerId()));
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, StatusUpdateRequest req) {
        Order order = getOrThrow(id);
        order.setStatus(req.getStatus());
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse addItem(UUID orderId, OrderItemRequest req) {
        Order order = getOrThrow(orderId);
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductName(req.getProductName());
        item.setQuantity(req.getQuantity());
        item.setUnitPrice(req.getUnitPrice());
        item.computeSubtotal();
        order.getItems().add(item);
        order.recalculateTotal();
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse removeItem(UUID orderId, UUID itemId) {
        Order order = getOrThrow(orderId);
        boolean removed = order.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        order.recalculateTotal();
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public void delete(UUID id) {
        getOrThrow(id);
        orderRepository.deleteById(id);
    }

    private Order getOrThrow(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }
}
