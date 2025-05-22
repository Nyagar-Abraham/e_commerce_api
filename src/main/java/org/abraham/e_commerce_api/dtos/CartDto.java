package org.abraham.e_commerce_api.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    private BigDecimal cartTotal = BigDecimal.ZERO;
    private UserDto customer;
    private List<CartItemDto> items = new ArrayList<>();
}
