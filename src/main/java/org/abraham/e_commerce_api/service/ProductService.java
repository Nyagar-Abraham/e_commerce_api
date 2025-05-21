package org.abraham.e_commerce_api.service;

import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.auth.AuthUtilities;
import org.abraham.e_commerce_api.dtos.AddProductRequest;
import org.abraham.e_commerce_api.dtos.ProductDto;
import org.abraham.e_commerce_api.entities.Category;
import org.abraham.e_commerce_api.entities.Product;
import org.abraham.e_commerce_api.entities.ProductStatus;
import org.abraham.e_commerce_api.entities.Role;
import org.abraham.e_commerce_api.exceptions.PermissionDeniedException;
import org.abraham.e_commerce_api.mappers.ProductMapper;
import org.abraham.e_commerce_api.repositories.CategoryRepository;
import org.abraham.e_commerce_api.repositories.ProductRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final AuthUtilities authUtilities;
    private final ProductMapper productMapper;
    private ProductRepository productRepository;

    public ProductDto addProduct(AddProductRequest request) {
       var role = authUtilities.getUserAuthorities();
       var seller = authUtilities.getCurrentUser();

       if(!role.equals(Role.SELLER.name())) {
           throw new PermissionDeniedException("Only sellers can add products");
       }
       var category = categoryRepository.findByName(request.getCategory()).orElse(null);

       if (category == null) {
           category = new Category(request.getCategory());
       }

       var product = Product.builder()
               .name(request.getName())
               .description(request.getDescription())
               .price(request.getPrice())
               .category(category)
               .quantity(request.getQuantity())
               .manufacturer(request.getManufacturer())
               .status(ProductStatus.AVAILABLE.name())
               .seller(seller)
               .build();


        productRepository.save(product);

        return productMapper.toDto(product);

    }
}
