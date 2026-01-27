package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.OrderStatus;
import com.judy.ecommerce.backend.dto.order.NewOrderDTO;
import com.judy.ecommerce.backend.dto.order.OrderDTO;
import com.judy.ecommerce.backend.dto.order.OrderProductDTO;
import com.judy.ecommerce.backend.dto.cart.CartProductDTO;
import com.judy.ecommerce.backend.entity.*;
import com.judy.ecommerce.backend.exception.ResourceNotFoundException;
import com.judy.ecommerce.backend.exception.UnauthorizedException;
import com.judy.ecommerce.backend.repository.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ConfigRepository configRepository;

    public CartService(UserRepository userRepository,
                       CartRepository cartRepository,
                       ProductRepository productRepository,
                       OrderRepository orderRepository,
                       OrderProductRepository orderProductRepository, ConfigRepository configRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.configRepository = configRepository;
    }

    public List<CartProductDTO> getCart(String username) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        return getCartProducts(user);
    }

    public List<CartProductDTO> addToCart(String username, int id, int quantity) {
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

    public List<CartProductDTO> updateQuantityInCart(String username, int id, int quantity) {
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

    public List<CartProductDTO> removeFromCart(String username, int id) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        // Don't check for disabled to allow removing a disabled product
        Products product = productRepository.findById(id)
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

    public OrderDTO payOrder(String username, NewOrderDTO orderInfos) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected error."));

        List<Carts> cartProducts = cartRepository.findAllByUser(user);

        // Get the current shipping fees
        AppConfig config = configRepository.findByConfigName("shippingFees").orElseThrow();
        double shippingFees = Double.parseDouble(config.getConfigValue());

        if (cartProducts.isEmpty()) {
            throw new UnauthorizedException("Cart is empty.");
        }

        // Check if requested quantity is still ok
        // Also check for disabled products
        for (Carts cart : cartProducts) {
            Products product = cart.getProduct();

            if (cart.getProduct().isDisabled()) {
                throw new UnauthorizedException(product.getName() + ": This product cannot be ordered anymore.");
            }

            checkQuantity(product, cart.getQuantity());
        }

        // Decrease stock for each product
        for (Carts cart : cartProducts) {
            Products product = cart.getProduct();
            product.setQuantity(product.getQuantity() - cart.getQuantity());
            productRepository.save(product);
        }

        // Create the order
        Orders order = new Orders();
        order.setUser(user);
        order.setDeliveryLastName(orderInfos.deliveryLastName());
        order.setDeliveryFirstName(orderInfos.deliveryFirstName());
        order.setDeliveryAddress(orderInfos.deliveryAddress());
        order.setShippingFees(shippingFees);
        order.setStatus(OrderStatus.NOT_CONFIRMED);
        orderRepository.save(order);

        // Move products from cart to order
        List<OrderProducts> orderProducts = new ArrayList<>();
        for (Carts cartProduct : cartProducts) {
            OrderProducts orderProduct = new OrderProducts();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartProduct.getProduct());
            orderProduct.setPricePaid(cartProduct.getProduct().getSalePrice());
            orderProduct.setQuantity(cartProduct.getQuantity());
            orderProductRepository.save(orderProduct);
            cartRepository.delete(cartProduct);
            orderProducts.add(orderProduct);
        }

        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                getOrderProducts(orderProducts),
                orderProductRepository.getPriceSumByOrder(order),
                order.getShippingFees(),
                order.getDeliveryLastName(),
                order.getDeliveryFirstName(),
                order.getDeliveryAddress(),
                order.getStatus().toString(),
                order.getOrderedAt()
        );
    }


    // UTILS //

    private CartProductDTO productToDTO(Products product, int requestedQuantity) {
        return new CartProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getSalePrice(),
                product.getSalePercent(),
                requestedQuantity
        );
    }

    private List<CartProductDTO> getCartProducts(Users user) {
        List<CartProductDTO> products = new ArrayList<>();
        List<Carts> cart = cartRepository.findAllByUser(user);

        for (Carts cp : cart) {
            Products product = cp.getProduct();
            products.add(productToDTO(product, cp.getQuantity()));
        }

        return products;
    }

    private List<OrderProductDTO> getOrderProducts(List<OrderProducts> orderProducts) {
        List<OrderProductDTO> products = new ArrayList<>();

        for (OrderProducts op : orderProducts) {
            Products product = op.getProduct();
            products.add(new OrderProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getSalePrice(),
                    op.getQuantity()
            ));
        }

        return products;
    }

    private void checkQuantity(Products product, int quantityRequested) {
        if (product.getQuantity() - quantityRequested < 0) {
            throw new UnauthorizedException(product.getName() + ": Insufficient stock left.");
        }
    }
}