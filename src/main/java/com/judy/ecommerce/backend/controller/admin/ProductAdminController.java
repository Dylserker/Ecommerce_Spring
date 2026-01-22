package com.judy.ecommerce.backend.controller.admin;

import com.judy.ecommerce.backend.dto.product.NewProductDTO;
import com.judy.ecommerce.backend.dto.product.ProductDTO;
import com.judy.ecommerce.backend.dto.search.FiltersDTO;
import com.judy.ecommerce.backend.service.ProductService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProductAdmin() {
        return ResponseEntity.ok(productService.getAllProducts(true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductByIdAdmin(@PathVariable @Min(1) int id) {
        return ResponseEntity.ok(productService.getProductById(id, true));
    }

    @GetMapping("/search/{input}")
    public ResponseEntity<List<ProductDTO>> searchProductsAdmin(@PathVariable String input,
                                                                @RequestBody(required=false) FiltersDTO filters) {
        return ResponseEntity.ok(productService.searchProducts(input, filters, true));
    }

    @PostMapping()
    public ResponseEntity<ProductDTO> addNewProduct(@RequestBody NewProductDTO newProduct) {
        return ResponseEntity.status(201).body(productService.addNewProduct(newProduct));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable long id,
                                                  @RequestBody NewProductDTO editedProduct) {
        return ResponseEntity.ok(productService.editProduct(id, editedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
