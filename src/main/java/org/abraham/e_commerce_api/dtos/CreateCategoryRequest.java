package org.abraham.e_commerce_api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    @NotBlank(message = "category name is required")
    private String name;
}
