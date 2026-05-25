package com.oms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String phone;
    private String address;
}
