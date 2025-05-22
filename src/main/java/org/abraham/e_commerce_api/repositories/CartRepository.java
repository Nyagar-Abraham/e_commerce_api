package org.abraham.e_commerce_api.repositories;

import org.abraham.e_commerce_api.entities.Cart;
import org.abraham.e_commerce_api.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByCustomer(User user);

    @EntityGraph(attributePaths = "items.product.category")
    @Query("SELECT c FROM Cart c WHERE c.customer =:userId ")
    Optional<Cart> findByUserId(@Param("userId") User userId);
}