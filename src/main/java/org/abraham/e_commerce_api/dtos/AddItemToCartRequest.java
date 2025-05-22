package org.abraham.e_commerce_api.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "product is required")
    private Long productId;

    @NotNull(message = "quantity must be provided")
    private Integer quantity;
}
