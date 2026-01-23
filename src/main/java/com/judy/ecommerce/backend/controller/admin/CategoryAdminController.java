package com.judy.ecommerce.backend.controller.admin;

import com.judy.ecommerce.backend.dto.category.CategoryDTO;
import com.judy.ecommerce.backend.dto.category.NewCategoryDTO;
import com.judy.ecommerce.backend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public ResponseEntity<CategoryDTO> addNewCategory(@RequestBody NewCategoryDTO newCategory) {
        return ResponseEntity.status(201).body(categoryService.addNewCategory(newCategory));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDTO> editCategory(@PathVariable long id,
                                                    @RequestBody NewCategoryDTO editCategory) {
        return ResponseEntity.ok(categoryService.editCategory(id, editCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
