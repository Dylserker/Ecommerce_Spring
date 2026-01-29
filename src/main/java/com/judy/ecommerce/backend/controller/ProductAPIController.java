package com.judy.ecommerce.backend.controller;

import com.judy.ecommerce.backend.dto.product.ProductDTO;
import com.judy.ecommerce.backend.dto.filter.ProductFilterDTO;
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
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.getAllProducts(false, page));
    }

    @GetMapping("/sale")
    public ResponseEntity<List<ProductDTO>> getAllProductsInSale(@RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.getAllProductsInSale(false, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable @Min(1) int id) {
        return ResponseEntity.ok(productService.getProductById(id, false));
    }

    @GetMapping("/search/{input}")
    public ResponseEntity<List<ProductDTO>> searchProducts(@PathVariable String input,
                                                           @RequestBody(required=false) ProductFilterDTO filters,
                                                           @RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.searchProducts(input, filters, false, page));
    }
}
