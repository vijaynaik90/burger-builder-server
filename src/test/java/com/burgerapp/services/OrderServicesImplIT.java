package com.burgerapp.services;

import com.burgerapp.config.WithMockBasicAuth;
import com.burgerapp.domain.*;
import com.burgerapp.exception.AppException;
import com.burgerapp.helpers.TestDataProviders;
import com.burgerapp.mapper.OrderMapper;
import com.burgerapp.mapper.OrderMapperTest;
import com.burgerapp.model.OrderDTO;
import com.burgerapp.model.OrderDataDTO;
import com.burgerapp.repositories.OrderRepository;
import com.burgerapp.repositories.RoleRepository;
import com.burgerapp.repositories.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServicesImplIT {

    public static final String USERNAME = "foobar90";
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private OrderService underTest;

    private Order order;
    private OrderDTO inputDTO;


    public  User createUser() {
        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User role not set"));
        System.out.println("Creating user");

        // create user role first
//        Role role = new Role();
//        role.setName(RoleName.ROLE_USER);
//        Role savedRole = roleRepository.save(role);

        User user = TestDataProviders.createTestUser(false);
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Collections.singleton(role));
        User newUser = userRepository.save(user);
        return newUser;
    }
    @Before
    public void setUp() {
        //create user first and persisit user.
        // then write tests for create ,get and update burger
//        user = createUser();
        order = TestDataProviders.createOrderEntity(false);
        inputDTO = OrderMapper.INSTANCE.toOrderDTO(order);
//        underTest = new OrderServiceImpl(OrderMapperTest.INSTANCE,orderRepository,userRepository);
//        OrderDTO inputDTO = OrderMapperTest.INSTANCE.toOrderDTO(order);
//        OrderDTO result = underTest.createOrder(inputDTO,"foobar90");
    }

    @Test
    @Transactional
    public void shouldCreateOrder(){
        System.out.println("Executing Test1");
        OrderDTO result = underTest.createOrder(inputDTO,USERNAME);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void shouldfetchOrders(){
        System.out.println("Executing Test2");
        List<OrderDTO> result = underTest.getAllOrders();
        assertNotNull(result);
        assertEquals(result.size(),0);
    }

//
    @Test
    @Transactional
    @WithMockBasicAuth(username = USERNAME,roles = "ROLE_USER")
    public void shouldfetchOrdersByUserId(){
        System.out.println("Executing Test 3");
        underTest.createOrder(inputDTO,USERNAME);
        orderRepository.flush();
        List<OrderDTO> result = underTest.getOrdersByUserId("1");
        assertNotNull(result);
        assertEquals(result.size(),1);
    }



    @Test
    @Transactional
    @WithMockBasicAuth(username = USERNAME,roles = "ROLE_USER")
    public void shouldUpdateOrder(){
        System.out.println("Executing Test 4");
        // First create an order and then update.
        OrderDTO created= underTest.createOrder(inputDTO,USERNAME);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setArchived(false);
        orderDTO.setOrderData(new OrderDataDTO("UpdatedName","1234","20171","INDIA","updatedEmail@test.com",DeliveryMethod.CHEAPEST));
        OrderDTO updatedOrder = underTest.updateOrderById(created.getId(),orderDTO);
        assertNotNull(updatedOrder);
        assertFalse(updatedOrder.getArchived());
        assertEquals("UpdatedName",updatedOrder.getOrderData().getCustomerName());
    }
    
    @Test(expected = AccessDeniedException.class)
    @WithMockBasicAuth(username = USERNAME,roles = "ROLE_GUEST")
    public void callSecuredUpdateOrder_thenAccessDenied () {
        OrderDTO updatedDTO = underTest.updateOrderById(1L,inputDTO);
    }

}
