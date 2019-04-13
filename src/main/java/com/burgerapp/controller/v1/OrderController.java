package com.burgerapp.controller.v1;

import com.burgerapp.exception.BadRequestException;
import com.burgerapp.exception.ResourceNotFoundException;
import com.burgerapp.model.OrderDTO;
import com.burgerapp.model.OrderListDTO;
import com.burgerapp.security.CurrentUser;
import com.burgerapp.security.UserPrincipal;
import com.burgerapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize(value="hasRole('USER')")
    public ResponseEntity<OrderDTO> createOrder(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO result = orderService.createOrder(orderDTO,currentUser.getUsername());
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<OrderListDTO> getAllOrders(){
        List<OrderDTO> list = orderService.getAllOrders();
        return new ResponseEntity<>(new OrderListDTO(list),HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize(value="hasRole('USER')")
    public ResponseEntity<OrderListDTO> getOrdersByUserId(@PathVariable String userId,@RequestParam( defaultValue = "false",required = false)  Boolean archived){
        List<OrderDTO> list = null;

        try {
            if (!archived) {
                list = orderService.getOrdersByUserId(userId);
            } else {
                list = orderService.getArchivedOrdersByUserId(userId);
            }
            return new ResponseEntity<>(new OrderListDTO(list),HttpStatus.OK);
        } catch(BadRequestException be) {
            return new ResponseEntity<>(new OrderListDTO(list),HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping({"/{orderId}"})
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO result = null;
        try{
            result = orderService.getOrder(orderId);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (ResourceNotFoundException re){
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    //TODO: maybe a patch method
    @PutMapping({"/{orderId}"})
    public ResponseEntity<OrderDTO> putOrderById(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        OrderDTO result = null;
        try{
            result = orderService.updateOrderById(orderId,orderDTO);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (ResourceNotFoundException re){
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
}
