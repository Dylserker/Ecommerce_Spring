package com.judy.ecommerce.backend.controller;

import com.judy.ecommerce.backend.dto.product.ProductDTO;
import com.judy.ecommerce.backend.dto.search.FiltersDTO;
import com.judy.ecommerce.backend.service.ProductService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductAPIController {

    private final ProductService productService;

    public ProductAPIController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts(false));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable @Min(1) int id) {
        return ResponseEntity.ok(productService.getProductById(id, false));
    }

    @GetMapping("/search/{input}")
    public ResponseEntity<List<ProductDTO>> searchProducts(@PathVariable String input,
                                                           @RequestBody(required=false) FiltersDTO filters) {
        return ResponseEntity.ok(productService.searchProducts(input, filters, false));
    }
}
