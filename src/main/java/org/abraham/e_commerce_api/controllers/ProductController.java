package org.abraham.e_commerce_api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.dtos.*;
import org.abraham.e_commerce_api.entities.Category;
import org.abraham.e_commerce_api.exceptions.CategoryExistsException;
import org.abraham.e_commerce_api.exceptions.PermissionDeniedException;
import org.abraham.e_commerce_api.repositories.CategoryRepository;
import org.abraham.e_commerce_api.service.CartService;
import org.abraham.e_commerce_api.service.ProductService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CartService cartService;

    @PostMapping("/{category_id}")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody AddProductRequest request,@PathVariable("category_id") Long category_id , UriComponentsBuilder uriBuilder) throws BadRequestException {
       var ProductDto = productService.addProduct(request,category_id);
       var uri = uriBuilder.path("/products/{id}").buildAndExpand(ProductDto.getId()).toUri();
       return ResponseEntity.created(uri).body(ProductDto);
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest request, UriComponentsBuilder uriBuilder) {
     var categoryDto = productService.createCategory(request.getName());
     return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("product_id") Long product_id) throws BadRequestException {
        var productDto = productService.getProduct(product_id);
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }

    @GetMapping("/seller/{user_id}")
    public ResponseEntity<List<ProductDto>> getSellersProducts(@PathVariable("user_id") Long user_id) throws BadRequestException {
       var productDtoList = productService.getUsersProducts(user_id);
      return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/category/{category_id}")
    public ResponseEntity<List<ProductDto>> getCategoryProducts(@PathVariable("category_id") Long category_id){
        var productDtoList = productService.getProductsByCategory(category_id);
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(){
        var productDtoList = productService.getProducts();
        return ResponseEntity.ok(productDtoList);
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody UpdateProductRequest request, @PathVariable("product_id") Long productId) throws BadRequestException {
       var productDto = productService.updateProduct(request,productId);
       return ResponseEntity.ok(productDto);
    }

    @PutMapping("/add/{product_id}")
    public ResponseEntity<ProductDto> incrementProductQuantity(@PathVariable("product_id") Long productId ) throws BadRequestException {
        var productDto = productService.incrementQuantity(productId);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping("/sub/{product_id}")
    public ResponseEntity<ProductDto> decrementProductQuantity(@PathVariable("product_id") Long productId ) throws BadRequestException {
        var productDto = productService.decrementQuantity(productId);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("product_id") Long productId) throws BadRequestException {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> handlePermissionDeniedException(PermissionDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
               "message", e.getMessage()
        ) );
    }

    @ExceptionHandler(CategoryExistsException.class)
    public ResponseEntity<?> CategoryExistsException(CategoryExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "message", e.getMessage()
        ));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> BadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", e.getMessage()
        ));
    }
}
