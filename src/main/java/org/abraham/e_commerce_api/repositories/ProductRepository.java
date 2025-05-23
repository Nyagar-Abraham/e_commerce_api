package org.abraham.e_commerce_api.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.abraham.e_commerce_api.entities.Product;
import org.abraham.e_commerce_api.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    List<Product> findAllBySeller(User user);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM Product p WHERE p.category.id=:category_id")
    List<Product> findByCategoryId(@Param("category_id") Long categoryId);
}