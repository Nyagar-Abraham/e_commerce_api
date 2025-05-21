package org.abraham.e_commerce_api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.dtos.AddProductRequest;
import org.abraham.e_commerce_api.dtos.ProductDto;
import org.abraham.e_commerce_api.exceptions.PermissionDeniedException;
import org.abraham.e_commerce_api.repositories.ProductRepository;
import org.abraham.e_commerce_api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;


    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody AddProductRequest request, UriComponentsBuilder uriBuilder) {
       var ProductDto = productService.addProduct(request);
       var uri = uriBuilder.path("/products/{id}").buildAndExpand(ProductDto.getId()).toUri();
       return ResponseEntity.created(uri).body(ProductDto);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> handlePermissionDeniedException(PermissionDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
