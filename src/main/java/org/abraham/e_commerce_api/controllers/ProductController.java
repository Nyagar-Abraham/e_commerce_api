package org.abraham.e_commerce_api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.dtos.AddProductRequest;
import org.abraham.e_commerce_api.dtos.CategoryDto;
import org.abraham.e_commerce_api.dtos.CreateCategoryRequest;
import org.abraham.e_commerce_api.dtos.ProductDto;
import org.abraham.e_commerce_api.entities.Category;
import org.abraham.e_commerce_api.exceptions.CategoryExistsException;
import org.abraham.e_commerce_api.exceptions.PermissionDeniedException;
import org.abraham.e_commerce_api.repositories.CategoryRepository;
import org.abraham.e_commerce_api.service.ProductService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;


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
