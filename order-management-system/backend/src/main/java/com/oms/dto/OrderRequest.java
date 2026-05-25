package com.oms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequest {
    @NotNull
    private UUID customerId;
}
