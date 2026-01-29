package com.judy.ecommerce.backend.controller.user;

import com.judy.ecommerce.backend.dto.order.OrderDTO;
import com.judy.ecommerce.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/order")
public class OrderAPIController {

    private final OrderService orderService;

    public OrderAPIController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestParam(required = false, defaultValue = "all") String status) {
        return ResponseEntity.ok(orderService.getAllOrders(userDetails.getUsername(), status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable long id) {
        return ResponseEntity.ok(orderService.getOrderById(userDetails.getUsername(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable long id) {
        orderService.cancelOrder(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
