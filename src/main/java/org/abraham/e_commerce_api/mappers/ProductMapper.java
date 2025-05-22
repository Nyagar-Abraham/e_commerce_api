package org.abraham.e_commerce_api.mappers;

import org.abraham.e_commerce_api.dtos.AddProductRequest;
import org.abraham.e_commerce_api.dtos.ProductDto;
import org.abraham.e_commerce_api.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product toEntity(AddProductRequest request);

    @Mapping(target = "category", source = "category.name")
    ProductDto toDto(Product product);
}
