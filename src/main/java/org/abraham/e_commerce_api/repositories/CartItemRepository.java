package org.abraham.e_commerce_api.repositories;

import org.abraham.e_commerce_api.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}