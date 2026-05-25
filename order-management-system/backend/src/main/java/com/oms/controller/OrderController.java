package com.oms.controller;

import com.oms.dto.*;
import com.oms.model.DeliveryStatus;
import com.oms.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;

    @GetMapping
    public List<OrderResponse> list(@RequestParam(required = false) DeliveryStatus status) {
        return service.findAll(status);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody StatusUpdateRequest req) {
        return service.updateStatus(id, req);
    }

    @PostMapping("/{id}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse addItem(@PathVariable UUID id, @Valid @RequestBody OrderItemRequest req) {
        return service.addItem(id, req);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public OrderResponse removeItem(@PathVariable UUID id, @PathVariable UUID itemId) {
        return service.removeItem(id, itemId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
