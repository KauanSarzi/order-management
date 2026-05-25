package com.oms.dto;

import com.oms.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    @NotNull
    private DeliveryStatus status;
}
