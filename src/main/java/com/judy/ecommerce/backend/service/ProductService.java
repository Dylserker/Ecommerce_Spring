package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.dto.product.NewProductDTO;
import com.judy.ecommerce.backend.dto.product.ProductDTO;
import com.judy.ecommerce.backend.dto.search.FiltersDTO;
import com.judy.ecommerce.backend.entity.Categories;
import com.judy.ecommerce.backend.entity.Products;
import com.judy.ecommerce.backend.exception.InvalidFormatException;
import com.judy.ecommerce.backend.exception.ResourceNotFoundException;
import com.judy.ecommerce.backend.repository.CategoryRepository;
import com.judy.ecommerce.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDTO> getAllProducts(boolean admin) {
        if (admin) {
            // Get all products, even if disabled
            return listToDTO(productRepository.findAll());
        }

        // Get all products, check for disabled
        return listToDTO(productRepository.findAllByDisabledFalse());
    }

    public ProductDTO getProductById(int id, boolean admin) {
        Products product;

        if (admin) {
            product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        } else {
            product = productRepository.findByIdAndDisabledFalse(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        }

        return productToDTO(product);
    }

    public List<ProductDTO> getAllProductsInSale(boolean admin) {
        if (admin) {
            // Get all products in sale, even if disabled
            return listToDTO(
                    productRepository.findAllBySalePercentGreaterThanOrderBySalePercentDesc(0)
            );
        } else {
            // Get all products in sale, check for disabled
            return listToDTO(
                    productRepository.findAllBySalePercentGreaterThanAndDisabledFalseOrderBySalePercentDesc(0)
            );
        }
    }

    public List<ProductDTO> searchProducts(String input, FiltersDTO filters, boolean admin) {
        // Make sure minPrice and maxPrice are correct
        if (filters.minPrice().isPresent()) {
            if (filters.minPrice().get() < 0) {
                throw new InvalidFormatException("Min price must be positive.");
            }
        }

        if (filters.maxPrice().isPresent()) {
            if (filters.maxPrice().get() <= 0) {
                throw new InvalidFormatException("Max price must be greater than 0.");
            }
        }

        if (filters.minPrice().isPresent() && filters.maxPrice().isPresent()) {
            if (filters.minPrice().get() > filters.maxPrice().get()) {
                throw new InvalidFormatException("Min price must be lower than max price.");
            }
        }

        List<Products> productsExact;
        List<Products> productsStarting;
        List<Products> productsContains;

        if (admin) {
            productsExact = productRepository.findAllByName(input);
            productsStarting = productRepository.findAllByNameStartingWith(input);
            productsContains = productRepository.findAllByNameContains(input);
        } else {
            productsExact = productRepository.findAllByNameAndDisabledFalse(input);
            productsStarting = productRepository.findAllByNameStartingWithAndDisabledFalse(input);
            productsContains = productRepository.findAllByNameContainsAndDisabledFalse(input);
        }
        List<Products> resultAll = new ArrayList<>(
                Stream.of(
                                productsExact,
                                productsStarting,
                                productsContains
                        )
                        .flatMap(List::stream)
                        .collect(Collectors.toMap(
                                Products::getId,
                                d -> d,
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ))
                        .values()
        );

        if (filters.minPrice().isPresent()) {
            resultAll = resultAll.stream()
                    .filter(p -> p.getPrice() >= filters.minPrice().get())
                    .toList();
        }

        if (filters.maxPrice().isPresent()) {
            resultAll = resultAll.stream()
                    .filter(p -> p.getPrice() <= filters.maxPrice().get())
                    .toList();
        }

        if (filters.categoryId().isPresent()) {
            resultAll = resultAll.stream()
                    .filter(p -> p.getCategory().getId() == filters.categoryId().get())
                    .toList();
        }

        return listToDTO(resultAll);
    }


    // ADMIN ONLY //

    public ProductDTO addNewProduct(NewProductDTO newProduct) {
        Products product = new Products();

        return setProduct(product, newProduct);
    }

    public ProductDTO editProduct(long id, NewProductDTO editedProduct) {
        // Check and get the product
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        return setProduct(product, editedProduct);
    }

    public void deleteProduct(long id) {
        // Check and get the product
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        productRepository.delete(product);
    }


    // UTILS //

    private ProductDTO productToDTO(Products product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getName(),
                product.getPrice(),
                product.getSalePrice(),
                product.getSalePercent(),
                product.getQuantity(),
                product.isDisabled()
        );
    }

    private List<ProductDTO> listToDTO(List<Products> listProducts) {
        List<ProductDTO> listDTO = new ArrayList<>();

        for (Products product : listProducts) {
            listDTO.add(productToDTO(product));
        }

        return listDTO;
    }

    private ProductDTO setProduct(Products targetProduct, NewProductDTO productInfos) {
        // Check and get the category, if not present, default to 1 -> undefined
        Categories category = categoryRepository.findById(
                productInfos.category().isPresent()
                        ? productInfos.category().get()
                        : 1
                )
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        targetProduct.setName(productInfos.name());
        targetProduct.setDescription(productInfos.description());
        targetProduct.setCategory(category);

        double price = productInfos.price();
        double salePrice;

        targetProduct.setPrice(price);

        if (productInfos.salePrice().isPresent()) {
            salePrice = productInfos.salePrice().get();

            // Readjust sale price if above base price
            if (salePrice > price) {
                salePrice = price;
            }
        } else {
            salePrice = productInfos.price();
        }

        targetProduct.setSalePrice(salePrice);
        targetProduct.setSalePercent((price - salePrice) * 100 / price);
        targetProduct.setQuantity(productInfos.quantity());
        targetProduct.setDisabled(productInfos.disabled());

        productRepository.save(targetProduct);

        return productToDTO(targetProduct);
    }
}
