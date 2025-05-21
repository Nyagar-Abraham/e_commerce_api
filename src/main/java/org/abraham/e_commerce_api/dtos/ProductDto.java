package org.abraham.e_commerce_api.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String quantity;
    private String manufacturer;
    private String category;
}
