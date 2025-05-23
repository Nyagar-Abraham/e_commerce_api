package org.abraham.e_commerce_api.service;

import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.auth.AuthUtilities;
import org.abraham.e_commerce_api.dtos.AddItemToCartRequest;
import org.abraham.e_commerce_api.dtos.CartDto;
import org.abraham.e_commerce_api.dtos.CartItemDto;
import org.abraham.e_commerce_api.dtos.ProductDto;
import org.abraham.e_commerce_api.entities.Cart;
import org.abraham.e_commerce_api.entities.CartItem;
import org.abraham.e_commerce_api.exceptions.CartExistsException;
import org.abraham.e_commerce_api.exceptions.CartNotFoundException;
import org.abraham.e_commerce_api.mappers.CartMapper;
import org.abraham.e_commerce_api.repositories.CartItemRepository;
import org.abraham.e_commerce_api.repositories.CartRepository;
import org.abraham.e_commerce_api.repositories.ProductRepository;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {


    private final CartRepository cartRepository;
    private final AuthUtilities authUtilities;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;


    @Transactional
    public CartDto createCart() {
        var user = authUtilities.getCurrentUser();

        if (cartRepository.findByCustomer(user).isPresent()) {
            throw new CartExistsException("User already has a cart");
        }

        Cart cart = new Cart();
        user.addCart(cart);
        userRepository.save(user);
        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartDto addToCart(AddItemToCartRequest request, UUID cartId) throws BadRequestException {
        System.out.println("running");
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new BadRequestException("Cart could not be found with the requested id");
        }
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            throw new BadRequestException("Product could not be found with the requested id");
        }

        var cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());
        cart.addCartItem(cartItem);
        try {
            cartRepository.save(cart);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        }


        return cartMapper.toDto(cart);
    }

    public List<CartItemDto> getUsersCartItems(Long userId) throws BadRequestException {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BadRequestException("No user found with the provided id");
        }

        var cart = cartRepository.findByUserId(user).orElse(null);

        if (cart == null) {
            throw new BadRequestException("No cart found with the provided id");
        }

        System.out.println("running");
        return cart.getItems().stream().map(cartMapper::toItemDto).toList();
    }

    public CartDto deleteItem(Long itemId) {
        var cart = getUserCart();
        cart.getItems().removeIf(cartItem -> cartItem.getId().equals(itemId));
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    private Cart getUserCart() {
        var user = authUtilities.getCurrentUser();
        var cart = cartRepository.findByCustomer(user).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("User cart not found");
        }
        return cart;
    }

    public CartDto clear() {
        var cart = getUserCart();
        cart.getItems().clear();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }


}
