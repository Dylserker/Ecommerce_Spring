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

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<List<ProductDto>> addOneToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable int id) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, 1));
    }

    @PostMapping("/{id}/{quantity}")
    public ResponseEntity<List<ProductDto>> addMultipleToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable int id,
                                                              @PathVariable @Min(1) int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, quantity));
    }

    @PatchMapping("/{id}/{quantity}")
    public ResponseEntity<List<ProductDto>> updateQuantityInCart(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable int id,
                                                                 @PathVariable @Min(0) int quantity) {
        return ResponseEntity.ok(cartService.updateQuantityInCart(userDetails.getUsername(), id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ProductDto>> removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable int id) {
        return ResponseEntity.ok(cartService.removeFromCart(userDetails.getUsername(), id));
    }
}
