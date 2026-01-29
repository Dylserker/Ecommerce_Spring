package com.judy.ecommerce.backend.controller.admin;

import com.judy.ecommerce.backend.OrderStatus;
import com.judy.ecommerce.backend.dto.order.OrderDTO;
import com.judy.ecommerce.backend.dto.search.SearchOrdersDTO;
import com.judy.ecommerce.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
public class OrderAdminController {

    private final OrderService orderService;

    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<SearchOrdersDTO> getAllOrdersAdmin(@RequestParam(required = false, defaultValue = "all") String status,
                                                             @RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(orderService.getAllOrdersAdmin(status, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderByIdAdmin(@PathVariable long id) {
        return ResponseEntity.ok(orderService.getOrderByIdAdmin(id));
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<OrderDTO> setOrderStatusAdmin(@PathVariable long id,
                                                        @PathVariable String status) {
        return ResponseEntity.ok(orderService.setOrderStatusAdmin(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrderAdmin(@PathVariable long id) {
        orderService.cancelOrderAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
