package org.abraham.e_commerce_api.service;

import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.auth.AuthUtilities;
import org.abraham.e_commerce_api.dtos.AddProductRequest;
import org.abraham.e_commerce_api.dtos.CategoryDto;
import org.abraham.e_commerce_api.dtos.ProductDto;
import org.abraham.e_commerce_api.dtos.UpdateProductRequest;
import org.abraham.e_commerce_api.entities.Category;
import org.abraham.e_commerce_api.entities.Product;
import org.abraham.e_commerce_api.entities.ProductStatus;
import org.abraham.e_commerce_api.entities.Role;
import org.abraham.e_commerce_api.exceptions.CategoryExistsException;
import org.abraham.e_commerce_api.exceptions.PermissionDeniedException;
import org.abraham.e_commerce_api.mappers.ProductMapper;
import org.abraham.e_commerce_api.repositories.CategoryRepository;
import org.abraham.e_commerce_api.repositories.ProductRepository;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final AuthUtilities authUtilities;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private ProductRepository productRepository;

    public ProductDto addProduct(AddProductRequest request, Long categoryId) throws BadRequestException {
        isSeller("Only sellers can add products");
        var seller = authUtilities.getCurrentUser();
        var category = categoryRepository.findById(categoryId).orElse(null);

        if (category == null) {
            throw new BadRequestException("Category not found");
        }

        var product = productRepository.findByName(request.getName()).orElse(null);
        if (product != null) {
            throw new BadRequestException("Product already exists");
        } else {
            product = Product.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .category(category)
                    .quantity(request.getQuantity())
                    .manufacturer(request.getManufacturer())
                    .status(ProductStatus.AVAILABLE)
                    .seller(seller)
                    .build();
        }

        productRepository.save(product);
        return productMapper.toDto(product);

    }


    public CategoryDto createCategory(String categoryName) {
        isSeller("Only sellers can add product category");
        if (categoryRepository.findByName(categoryName).isPresent()) {
            throw new CategoryExistsException("Category already created");
        }

        var category = new Category(categoryName);
        categoryRepository.save(category);
        return new CategoryDto(category.getId(), category.getName());
    }

    private void isSeller(String message) {
        var role = authUtilities.getUserAuthorities();
        if (!role.equals(Role.SELLER.name())) {
            throw new PermissionDeniedException(message);
        }
    }

    public List<ProductDto> getUsersProducts(Long userId) throws BadRequestException {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BadRequestException("Seller id is invalid");
        }
        var products = productRepository.findAllBySeller(user);
        return products.stream().map(productMapper::toDto).toList();
    }

    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    public ProductDto getProduct(Long productId) throws BadRequestException {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new BadRequestException("Product with the provided id not found");
        }
        return productMapper.toDto(product);
    }


    public List<ProductDto> getProductsByCategory(Long categoryId) {
        var products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(productMapper::toDto).toList();
    }

    public ProductDto updateProduct(UpdateProductRequest request, Long productId) throws BadRequestException {
        isSeller("Only sellers can update product category");
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new BadRequestException("Product with provided id not found");
        }
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setManufacturer(request.getManufacturer());
        product.setStatus(request.getStatus());
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public ProductDto incrementQuantity(Long productId) throws BadRequestException {
        isSeller("Only sellers can increment quantity");
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new BadRequestException("Provided a valid productId");
        }
        product.setQuantity(product.getQuantity() + 1);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDto decrementQuantity(Long productId) throws BadRequestException {
        isSeller("Only sellers can increment quantity");
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new BadRequestException("Provided a valid productId");
        }
        if (product.getQuantity() <= 0) {
            throw new IllegalStateException("Product is out of stock");
        } else {
            product.setQuantity(product.getQuantity() - 1);
        }

        productRepository.save(product);
        return productMapper.toDto(product);
    }
}
