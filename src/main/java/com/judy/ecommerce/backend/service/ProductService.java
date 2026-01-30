package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.dto.product.NewProductDTO;
import com.judy.ecommerce.backend.dto.product.ProductDTO;
import com.judy.ecommerce.backend.dto.filter.ProductFilterDTO;
import com.judy.ecommerce.backend.dto.search.PaginationDTO;
import com.judy.ecommerce.backend.dto.search.SearchProductsDTO;
import com.judy.ecommerce.backend.entity.Categories;
import com.judy.ecommerce.backend.entity.Products;
import com.judy.ecommerce.backend.exception.InvalidFormatException;
import com.judy.ecommerce.backend.exception.ResourceNotFoundException;
import com.judy.ecommerce.backend.repository.CategoryRepository;
import com.judy.ecommerce.backend.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public SearchProductsDTO getAllProducts(boolean admin, int page) {
        // Defaults to 20 elements per page
        Pageable pageable = PageRequest.of(page - 1, 20);

        if (admin) {
            // Get all products, even if disabled
            return toSearchDTO(
                    productRepository.findAllBy(pageable),
                    productRepository.countAllBy(),
                    pageable
            );
        }

        // Get all products, check for disabled
        return toSearchDTO(
                productRepository.findAllByDisabledFalse(pageable),
                productRepository.countAllByDisabledFalse(),
                pageable
        );
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

    public SearchProductsDTO getAllProductsInSale(boolean admin, int page) {
        // Defaults to 20 elements per page
        Pageable pageable = PageRequest.of(page - 1, 20);

        if (admin) {
            // Get all products in sale, even if disabled
            return toSearchDTO(
                    productRepository.findAllBySalePercentGreaterThanOrderBySalePercentDesc(0, pageable),
                    productRepository.countAllBySalePercentGreaterThan(0),
                    pageable
            );
        } else {
            // Get all products in sale, check for disabled
            return toSearchDTO(
                    productRepository.findAllBySalePercentGreaterThanAndDisabledFalseOrderBySalePercentDesc(0, pageable),
                    productRepository.countAllBySalePercentGreaterThanAndDisabledFalse(0),
                    pageable
            );
        }
    }

    public SearchProductsDTO searchProducts(String input, ProductFilterDTO filters, boolean admin, int page) {
        // Defaults to 20 elements per page
        Pageable pageable = PageRequest.of(page - 1, 20);

        // Init everything
        // -1 means "null" in this case
        double minPrice = -1;
        double maxPrice = -1;
        int categoryId = -1;
        // 0 to allow looking for products with stock >= 0
        int onlyInStock = 0;

        // If filters is not null
        if (filters != null) {
            // Check data and reassign variables
            if (filters.minPrice().isPresent()) {
                minPrice = filters.minPrice().get();

                if (minPrice < 0) {
                    throw new InvalidFormatException("Min price must be positive.");
                }
            }

            if (filters.maxPrice().isPresent()) {
                maxPrice = filters.maxPrice().get();

                if (maxPrice <= 0) {
                    throw new InvalidFormatException("Max price must be greater than 0.");
                }
            }

            if (filters.minPrice().isPresent() && filters.maxPrice().isPresent()) {
                if (filters.minPrice().get() > filters.maxPrice().get()) {
                    throw new InvalidFormatException("Min price must be lower than max price.");
                }
            }

            if (filters.categoryId().isPresent()) {
                categoryId = filters.categoryId().get();
            }

            if (filters.onlyInStock().isPresent()) {
                if (filters.onlyInStock().get()) {
                    onlyInStock = 1;
                }
            }
        }

        List<Products> resultAll;
        int countAll;

        if (admin) {
            resultAll = productRepository.findAllByNameLikeAndSearchOptions(
                    input,
                    minPrice,
                    maxPrice,
                    categoryId,
                    onlyInStock,
                    pageable
            );
            countAll = productRepository.countAllByNameLikeAndSearchOptions(
                    input,
                    minPrice,
                    maxPrice,
                    categoryId,
                    onlyInStock
            );
        } else {
            resultAll = productRepository.findAllByNameLikeAndSearchOptionsAndDisabledFalse(
                    input,
                    minPrice,
                    maxPrice,
                    categoryId,
                    onlyInStock,
                    pageable
            );
            countAll = productRepository.countAllByNameLikeAndSearchOptionsAndDisabledFalse(
                    input,
                    minPrice,
                    maxPrice,
                    categoryId,
                    onlyInStock
            );
        }

        return toSearchDTO(resultAll, countAll, pageable);
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

    private SearchProductsDTO toSearchDTO(List<Products> listProducts, int countProducts, Pageable pageable) {
        return new SearchProductsDTO(
                listToDTO(listProducts),
                new PaginationDTO(
                        pageable.getPageNumber() + 1,
                        // Always have at least 1 page even if 0 items
                        Math.max(
                                (int) Math.ceil((double) countProducts / pageable.getPageSize()),
                                1
                        ),
                        listProducts.size(),
                        countProducts
                )
        );
    }
}
