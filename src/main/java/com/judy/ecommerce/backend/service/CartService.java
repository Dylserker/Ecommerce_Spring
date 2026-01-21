package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.dto.product.ProductDto;
import com.judy.ecommerce.backend.entity.Carts;
import com.judy.ecommerce.backend.entity.Products;
import com.judy.ecommerce.backend.entity.Users;
import com.judy.ecommerce.backend.exception.ResourceNotFoundException;
import com.judy.ecommerce.backend.exception.UnauthorizedException;
import com.judy.ecommerce.backend.repository.CartRepository;
import com.judy.ecommerce.backend.repository.ProductRepository;
import com.judy.ecommerce.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(UserRepository userRepository,
                       CartRepository cartRepository,
                       ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<ProductDto> addToCart(String username, int id, int quantity) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        // Check if the product isn't already in cart
        boolean alreadyInCart = cartRepository.existsByUserAndProduct(user, product);

        if (alreadyInCart) {
            // If in cart, increase its quantity

            // Shouldn't fail thanks to the check above
            Carts oldCartProduct = cartRepository.findByUserAndProduct(user, product)
                    .orElseThrow();

            // Check if requested quantity + old quantity is ok
            if (!checkQuantity(product, quantity + oldCartProduct.getQuantity())) {
                throw new UnauthorizedException("Requested product quantity is too high.");
            }

            // Increase quantity and save
            oldCartProduct.setQuantity(oldCartProduct.getQuantity() + quantity);
            cartRepository.save(oldCartProduct);
        } else {
            // If not in cart, create a new cart item

            // Check if requested quantity is ok
            if (!checkQuantity(product, quantity)) {
                throw new UnauthorizedException("Requested product quantity is too high.");
            }

            // Create the cart item and save it
            Carts productToAdd = new Carts();
            productToAdd.setUser(user);
            productToAdd.setProduct(product);
            productToAdd.setQuantity(quantity);
            cartRepository.save(productToAdd);
        }

        // Return the full cart formatted properly
        return getCartProducts(user);
    }

    public boolean checkQuantity(Products product, int quantityRequested) {
        return product.getQuantity() - quantityRequested >= 0;
    }

    public List<ProductDto> getCartProducts(Users user) {
        List<ProductDto> products = new ArrayList<>();
        List<Carts> cart = cartRepository.findAllByUser(user);

        for (Carts cartProduct : cart) {
            Products product = cartProduct.getProduct();
            products.add(new ProductDto(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategory().getName(),
                    product.getPrice(),
                    cartProduct.getQuantity()
            ));
        }

        return products;
    }
}