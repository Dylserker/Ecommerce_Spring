package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.OrderStatus;
import com.judy.ecommerce.backend.dto.order.OrderDTO;
import com.judy.ecommerce.backend.dto.order.OrderProductDTO;
import com.judy.ecommerce.backend.entity.OrderProducts;
import com.judy.ecommerce.backend.entity.Orders;
import com.judy.ecommerce.backend.entity.Products;
import com.judy.ecommerce.backend.entity.Users;
import com.judy.ecommerce.backend.exception.BadRequestException;
import com.judy.ecommerce.backend.exception.ConflictException;
import com.judy.ecommerce.backend.exception.ResourceNotFoundException;
import com.judy.ecommerce.backend.repository.OrderProductRepository;
import com.judy.ecommerce.backend.repository.OrderRepository;
import com.judy.ecommerce.backend.repository.ProductRepository;
import com.judy.ecommerce.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        OrderProductRepository orderProductRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }

    public List<OrderDTO> getAllOrders(String username, String status) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow();

        try {
            // Try to return the desired status
            return listToDTO(orderRepository.findAllByUserAndStatusOrderByOrderedAtDesc(user, OrderStatus.valueOf(status.toUpperCase())));
        } catch (IllegalArgumentException e) {
            // Fallback to any if no/wrong status
            return listToDTO(orderRepository.findAllByUserOrderByOrderedAtDesc(user));
        }
    }

    public OrderDTO getOrderById(String username, long id) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
        .orElseThrow();

        Orders order = orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        return orderToDTO(order);
    }

    public void cancelOrder(String username, long id) {
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(username)
                .orElseThrow();

        Orders order = orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        doCancel(order, false);
    }


    // ADMIN //

    public List<OrderDTO> getAllOrdersAdmin(String status) {
        try {
            // Try to return the desired status
            return listToDTO(orderRepository.findAllByStatusOrderByOrderedAtDesc(OrderStatus.valueOf(status.toUpperCase())));
        } catch (IllegalArgumentException e) {
            // Fallback to any if no/wrong status
            return listToDTO(orderRepository.findAllByOrderByOrderedAtDesc());
        }
    }

    public OrderDTO getOrderByIdAdmin(long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        return orderToDTO(order);
    }

    public void cancelOrderAdmin(long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        doCancel(order, true);
    }

    public OrderDTO setOrderStatusAdmin(long id, String statusStr) {
        try {
            // Get the status of the order
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());

            // Disallow canceling order this way
            if (status == OrderStatus.CANCELED) {
                throw new BadRequestException("Cannot cancel an order this way, use the DELETE route instead.");
            }

            // Get the order
            Orders order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

            // Disallow changing status of a canceled order
            if (order.getStatus() == OrderStatus.CANCELED) {
                throw new ConflictException("Cannot restore an order.");
            }

            order.setStatus(status);
            orderRepository.save(order);

            return orderToDTO(order);
        } catch (IllegalArgumentException e) {
            // Throw a personalized error
            throw new BadRequestException("Invalid status.");
        }
    }


    // UTILS //

    private void doCancel(Orders order, boolean admin) {
        // Can only cancel if order is not prepared yet
        if (!order.getStatus().isCancellable()) {
            String msg = "This order cannot be canceled anymore.";
            if (admin) {
                msg += " Admin: if that's a mistake, change the order status first.";
            }

            // Different message if already canceled
            if (order.getStatus() == OrderStatus.CANCELED) {
                msg = "This order is already canceled.";
            }

            throw new ConflictException(msg);
        }

        // Restore products stock, but don't delete the order products
        // And keep the old requested quantity as canceled order can still be viewed
        order.setStatus(OrderStatus.CANCELED);
        for (OrderProducts orderProduct : order.getOrderProducts()) {
            Products product = orderProduct.getProduct();
            product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
    }

    private OrderDTO orderToDTO(Orders order) {
        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                getOrderProducts(order.getOrderProducts()),
                orderProductRepository.getPriceSumByOrder(order),
                order.getShippingFees(),
                order.getDeliveryLastName(),
                order.getDeliveryFirstName(),
                order.getDeliveryAddress(),
                order.getStatus().toString(),
                order.getOrderedAt()
        );
    }

    public static List<OrderProductDTO> getOrderProducts(List<OrderProducts> orderProducts) {
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

    private List<OrderDTO> listToDTO(List<Orders> listOrders) {
        List<OrderDTO> listDTO = new ArrayList<>();

        for (Orders order : listOrders) {
            listDTO.add(orderToDTO(order));
        }

        return listDTO;
    }
}
