package com.burgerapp.services;

import com.burgerapp.model.OrderDTO;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO ordersDTO,String username);

    OrderDTO getOrder(Long id);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByUserId(String userId);

    @Secured("ROLE_USER")
    List<OrderDTO> getArchivedOrdersByUserId(String userId);

    OrderDTO updateOrderById(Long orderId,OrderDTO putData);
}
