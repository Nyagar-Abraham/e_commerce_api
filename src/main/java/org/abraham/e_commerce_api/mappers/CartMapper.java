package org.abraham.e_commerce_api.mappers;


import org.abraham.e_commerce_api.dtos.CartDto;
import org.abraham.e_commerce_api.dtos.CartItemDto;
import org.abraham.e_commerce_api.entities.Cart;
import org.abraham.e_commerce_api.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class,ProductMapper.class})
public interface CartMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "cartTotal", expression = "java(cart.getCartTotal())")
    @Mapping(target = "items",source = "items")
    CartDto toDto(Cart cart);


    CartItemDto toItemDto(CartItem cartItem);
}

