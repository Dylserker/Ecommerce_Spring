package com.judy.ecommerce.backend.controller.user;

import com.judy.ecommerce.backend.dto.order.NewOrderDTO;
import com.judy.ecommerce.backend.dto.order.OrderDTO;
import com.judy.ecommerce.backend.dto.cart.CartProductDTO;
import com.judy.ecommerce.backend.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
public class CartAPIController {

    private final CartService cartService;

    public CartAPIController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping()
    public ResponseEntity<List<CartProductDTO>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<List<CartProductDTO>> addOneToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable int id) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, 1));
    }

    @PostMapping("/{id}/{quantity}")
    public ResponseEntity<List<CartProductDTO>> addMultipleToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable int id,
                                                              @PathVariable @Min(1) int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, quantity));
    }

    @PatchMapping("/{id}/{quantity}")
    public ResponseEntity<List<CartProductDTO>> updateQuantityInCart(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable int id,
                                                                 @PathVariable @Min(0) int quantity) {
        return ResponseEntity.ok(cartService.updateQuantityInCart(userDetails.getUsername(), id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<CartProductDTO>> removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable int id) {
        return ResponseEntity.ok(cartService.removeFromCart(userDetails.getUsername(), id));
    }

    @PostMapping("/payment")
    public ResponseEntity<OrderDTO> payOrder(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody @Valid NewOrderDTO newOrder) {
        return ResponseEntity.ok(cartService.payOrder(userDetails.getUsername(), newOrder));
    }
}
