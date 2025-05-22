package org.abraham.e_commerce_api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.dtos.AddItemToCartRequest;
import org.abraham.e_commerce_api.dtos.CartDto;
import org.abraham.e_commerce_api.dtos.CartItemDto;
import org.abraham.e_commerce_api.dtos.ErrorDto;
import org.abraham.e_commerce_api.exceptions.CartExistsException;
import org.abraham.e_commerce_api.exceptions.CartNotFoundException;
import org.abraham.e_commerce_api.service.CartService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cart_id}")
    public ResponseEntity<CartDto> addToCart(@Valid @RequestBody AddItemToCartRequest request,@PathVariable("cart_id") UUID cart_id) throws BadRequestException {
        var cartDto = cartService.addToCart(request,cart_id);
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<CartItemDto>> getCustomersCartItems(
            @PathVariable("user_id") Long userId
    ) throws BadRequestException {
        var cartItemDtoList = cartService.getUsersCartItems(userId);
        return ResponseEntity.ok(cartItemDtoList);
    }

    @DeleteMapping("/{item_id}")
    public ResponseEntity<CartDto> deleteCartItem(@PathVariable("item_id") Long item_id) {
       var cartDto = cartService.deleteItem(item_id);
       return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartDto> clearCart() {
       var cartDto =  cartService.clear();
       return ResponseEntity.ok(cartDto);
    }

    @ExceptionHandler(CartExistsException.class)
    public ResponseEntity<?> cartExistsException(CartExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("message", e.getMessage())
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFoundException(CartNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto(e.getMessage())
        );
    }
}
