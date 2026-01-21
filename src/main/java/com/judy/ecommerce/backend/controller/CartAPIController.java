package com.judy.ecommerce.backend.controller;

import com.judy.ecommerce.backend.dto.product.ProductDto;
import com.judy.ecommerce.backend.service.CartService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartAPIController {

    private final CartService cartService;

    public CartAPIController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<List<ProductDto>> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable int id) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, 1));
    }

    @PostMapping("/add/{id}/{quantity}")
    public ResponseEntity<List<ProductDto>> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable int id,
                                                      @PathVariable @Min(1) int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, quantity));
    }
}
