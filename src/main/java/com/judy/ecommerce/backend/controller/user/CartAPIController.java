package com.judy.ecommerce.backend.controller.user;

import com.judy.ecommerce.backend.dto.order.OrderDTO;
import com.judy.ecommerce.backend.dto.product.ProductDTO;
import com.judy.ecommerce.backend.service.CartService;
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
    public ResponseEntity<List<ProductDTO>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<List<ProductDTO>> addOneToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable int id) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, 1));
    }

    @PostMapping("/{id}/{quantity}")
    public ResponseEntity<List<ProductDTO>> addMultipleToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable int id,
                                                              @PathVariable @Min(1) int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), id, quantity));
    }

    @PatchMapping("/{id}/{quantity}")
    public ResponseEntity<List<ProductDTO>> updateQuantityInCart(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable int id,
                                                                 @PathVariable @Min(0) int quantity) {
        return ResponseEntity.ok(cartService.updateQuantityInCart(userDetails.getUsername(), id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ProductDTO>> removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable int id) {
        return ResponseEntity.ok(cartService.removeFromCart(userDetails.getUsername(), id));
    }

//    @PostMapping("/payment")
//    public ResponseEntity<OrderDTO>
}
