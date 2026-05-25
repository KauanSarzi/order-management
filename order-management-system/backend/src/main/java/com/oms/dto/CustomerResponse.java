package com.oms.dto;

import com.oms.model.Customer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerResponse {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;

    public static CustomerResponse from(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.id = c.getId();
        r.name = c.getName();
        r.email = c.getEmail();
        r.phone = c.getPhone();
        r.address = c.getAddress();
        r.createdAt = c.getCreatedAt();
        return r;
    }
}
