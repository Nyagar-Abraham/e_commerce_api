package org.abraham.e_commerce_api.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AddProductRequest {
    @NotBlank(message = "name is required")
    @Size(min = 4, max = 100)
    private String name;
    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount")
    private BigDecimal price;
    @NotBlank(message = "description is required")
    @Size(min = 20, max = 2000)
    private String description;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000, message = "Quantity must not exceed 1000")
    private Integer quantity;
    @NotBlank(message = "manufacturer is required")
    private String manufacturer;
}
