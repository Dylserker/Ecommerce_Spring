package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.dto.category.CategoryDTO;
import com.judy.ecommerce.backend.dto.category.NewCategoryDTO;
import com.judy.ecommerce.backend.entity.Categories;
import com.judy.ecommerce.backend.entity.Products;
import com.judy.ecommerce.backend.exception.ResourceNotFoundException;
import com.judy.ecommerce.backend.exception.UnauthorizedException;
import com.judy.ecommerce.backend.repository.CategoryRepository;
import com.judy.ecommerce.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        // Get all categories
        return listToDTO(categoryRepository.findAll());
    }

    public CategoryDTO getCategoryById(int id) {
        Categories category;

        category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        return categoryToDTO(category);
    }


    // ADMIN ONLY //

    public CategoryDTO addNewCategory(NewCategoryDTO newCategory) {
        Categories category = new Categories();

        return setCategory(category, newCategory);
    }

    public CategoryDTO editCategory(long id, NewCategoryDTO editCategory) {
        if (id == 1) {
            throw new UnauthorizedException("Cannot edit the default category.");
        }

        // Check and get the category
        Categories category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        return setCategory(category, editCategory);
    }

    public void deleteCategory(long id) {
        if (id == 1) {
            throw new UnauthorizedException("Cannot delete the default category.");
        }

        // Check and get the category
        Categories category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        // Get the default category
        Categories defaultCategory = categoryRepository.findById(1)
                        .orElseThrow();

        // Get all products with this category
        List<Products> products = productRepository.findByCategory(category);

        // Set the default category to all found products
        for (Products product : products) {
            product.setCategory(defaultCategory);
            productRepository.save(product);
        }

        categoryRepository.delete(category);
    }


    // UTILS //

    private CategoryDTO categoryToDTO(Categories category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }

    private List<CategoryDTO> listToDTO(List<Categories> listCategories) {
        List<CategoryDTO> listDTO = new ArrayList<>();

        for (Categories category : listCategories) {
            listDTO.add(categoryToDTO(category));
        }

        return listDTO;
    }

    private CategoryDTO setCategory(Categories targetCategory, NewCategoryDTO categoryInfos) {
        targetCategory.setName(categoryInfos.name());
        categoryRepository.save(targetCategory);
        return categoryToDTO(targetCategory);
    }
}
