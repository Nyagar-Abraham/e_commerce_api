package org.abraham.e_commerce_api.dtos;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private ProductDto product;
}
