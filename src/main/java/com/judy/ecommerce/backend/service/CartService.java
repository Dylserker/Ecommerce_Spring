package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.dto.product.ProductDTO;
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

    public List<ProductDTO> getCart(String username) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        return getCartProducts(user);
    }

    public List<ProductDTO> addToCart(String username, int id, int quantity) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        Products product = productRepository.findByIdAndDisabledFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        // Check if the product isn't already in cart
        boolean alreadyInCart = cartRepository.existsByUserAndProduct(user, product);

        if (alreadyInCart) {
            // If in cart, increase its quantity

            // Shouldn't fail thanks to the check above
            Carts oldCartProduct = cartRepository.findByUserAndProduct(user, product)
                    .orElseThrow();

            // Check if requested quantity + old quantity is ok
            checkQuantity(product, quantity + oldCartProduct.getQuantity());

            // Increase quantity and save
            oldCartProduct.setQuantity(oldCartProduct.getQuantity() + quantity);
            cartRepository.save(oldCartProduct);
        } else {
            // If not in cart, create a new cart item

            // Check if requested quantity is ok
            checkQuantity(product, quantity);

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

    public List<ProductDTO> updateQuantityInCart(String username, int id, int quantity) {
        // Redirect to the delete function if quantity = 0
        if (quantity == 0) {
            return removeFromCart(username, id);
        }

        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        Products product = productRepository.findByIdAndDisabledFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (!cartRepository.existsByUserAndProduct(user, product)) {
            throw new ResourceNotFoundException("Product not in cart.");
        }

        // Check if requested quantity is ok
        checkQuantity(product, quantity);

        // Shouldn't fail
        Carts oldProduct = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow();

        oldProduct.setQuantity(quantity);
        cartRepository.save(oldProduct);

        // Return the full cart formatted properly
        return getCartProducts(user);
    }

    public List<ProductDTO> removeFromCart(String username, int id) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        Products product = productRepository.findByIdAndDisabledFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (!cartRepository.existsByUserAndProduct(user, product)) {
            throw new ResourceNotFoundException("Product not in cart.");
        }

        // Shouldn't fail
        Carts oldProduct = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow();

        cartRepository.delete(oldProduct);

        // Return the full cart formatted properly
        return getCartProducts(user);
    }


    // UTILS //

    private List<ProductDTO> getCartProducts(Users user) {
        List<ProductDTO> products = new ArrayList<>();
        List<Carts> cart = cartRepository.findAllByUser(user);

        for (Carts cartProduct : cart) {
            Products product = cartProduct.getProduct();
            products.add(ProductService.productToDTO(product));
        }

        return products;
    }

    private void checkQuantity(Products product, int quantityRequested) {
        if (product.getQuantity() - quantityRequested < 0) {
            throw new UnauthorizedException("Requested product quantity is too high.");
        }
    }
}