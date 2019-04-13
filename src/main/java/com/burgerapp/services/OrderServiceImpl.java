package com.burgerapp.services;

import com.burgerapp.domain.Order;
import com.burgerapp.domain.User;
import com.burgerapp.exception.BadRequestException;
import com.burgerapp.exception.ResourceNotFoundException;
import com.burgerapp.mapper.OrderDataMapper;
import com.burgerapp.mapper.OrderMapper;
import com.burgerapp.model.OrderDTO;
import com.burgerapp.repositories.OrderRepository;
import com.burgerapp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private UserRepository userRepository;

    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository,UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
    }


    @Override
    public OrderDTO createOrder(OrderDTO orderDTO,String username) {
        Order detachedOrder = orderMapper.toOrder(orderDTO);
        //TODO: get current user and store user for the order.
        User user = null;
        Optional<User> optionalUser = userRepository.findByUsername(username);
                if(!optionalUser.isPresent()) {
                    throw new ResourceNotFoundException("User cannot be found with username:"+ username);
                }else{
                    user = optionalUser.get();
                }
        detachedOrder.setUser(user);
        Order savedOrder = orderRepository.save(detachedOrder);
        return orderMapper.toOrderDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::toOrderDTO)
                    .orElseThrow(ResourceNotFoundException::new);


    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Secured("ROLE_USER")
    public List<OrderDTO> getOrdersByUserId(String userId) {
        Long id = parseUserId(userId);

        return orderRepository.findByUserIdAndUnArchived(id).stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Secured("ROLE_USER")
    public List<OrderDTO> getArchivedOrdersByUserId(String userId) {
        Long id = parseUserId(userId);
        List<Order> orderList = orderRepository.findByUserIdAndArchived(id);
                return orderList.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize(value = "hasRole('ROLE_USER')")
    public OrderDTO updateOrderById(Long orderId, OrderDTO putData) {
        //first get the order. If founf update it else throw exception.
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(order -> {
            if(putData.getArchived() != null) {
                order.setArchived(putData.getArchived());
            }
            if(putData.getOrderData()!=null) {
                order.setOrderData(OrderDataMapper.INSTANCE.toOrderData(putData.getOrderData()));
            }
            Order updatedOrder = orderRepository.save(order);
            return orderMapper.toOrderDTO(updatedOrder);

        }).orElseThrow(ResourceNotFoundException::new);
//        if(!optionalOrder.isPresent()) {
//            throw new ResourceNotFoundException("Cannot find order with ID:" +orderId);
//        }
//        Order order = optionalOrder.get();
//        //TODO: Currently only updating archived state for an order. Implement updating other fields as well.
//
//
//
//
//        return orderMapper.toOrderDTO(updatedOrder);
    }

    private Long parseUserId(String userId) {
        if(userId.equals('-')) {
            //TODO: maybe this logic has to change
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = (authentication != null) ? authentication.getName() : null;
        }
        Long id;
        try {
            id  = Long.parseLong(userId);
        } catch(NumberFormatException ex){
            throw new BadRequestException("Cannot parse userID String to long.UserID:" + userId);
        }
        return id;
    }
}
