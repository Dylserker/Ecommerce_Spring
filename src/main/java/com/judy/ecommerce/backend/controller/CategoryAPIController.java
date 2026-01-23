package com.judy.ecommerce.backend.controller;

import com.judy.ecommerce.backend.dto.category.CategoryDTO;
import com.judy.ecommerce.backend.service.CategoryService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryAPIController {

    private final CategoryService categoryService;

    public CategoryAPIController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable @Min(1) int id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}
