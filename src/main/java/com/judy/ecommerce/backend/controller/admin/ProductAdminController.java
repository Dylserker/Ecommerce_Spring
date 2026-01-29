package com.judy.ecommerce.backend.controller.admin;

import com.judy.ecommerce.backend.dto.product.NewProductDTO;
import com.judy.ecommerce.backend.dto.product.ProductDTO;
import com.judy.ecommerce.backend.dto.filter.ProductFilterDTO;
import com.judy.ecommerce.backend.dto.search.SearchProductsDTO;
import com.judy.ecommerce.backend.service.ProductService;
import jakarta.validation.Valid;
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
    public ResponseEntity<SearchProductsDTO> getAllProductsAdmin(@RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.getAllProducts(true, page));
    }

    @GetMapping("/sale")
    public ResponseEntity<SearchProductsDTO> getAllProductsInSale(@RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.getAllProductsInSale(true, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductByIdAdmin(@PathVariable @Min(1) int id) {
        return ResponseEntity.ok(productService.getProductById(id, true));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchProductsDTO> searchProductsAdmin(@RequestBody(required=false) @Valid ProductFilterDTO filters,
                                                                 @RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.searchProducts("", filters, true, page));
    }

    @GetMapping("/search/{input}")
    public ResponseEntity<SearchProductsDTO> searchProductsAdmin(@PathVariable String input,
                                                                 @RequestBody(required=false) @Valid ProductFilterDTO filters,
                                                                 @RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.searchProducts(input, filters, true, page));
    }

    @PostMapping()
    public ResponseEntity<ProductDTO> addNewProduct(@RequestBody @Valid NewProductDTO newProduct) {
        return ResponseEntity.status(201).body(productService.addNewProduct(newProduct));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable long id,
                                                  @RequestBody @Valid NewProductDTO editedProduct) {
        return ResponseEntity.ok(productService.editProduct(id, editedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
