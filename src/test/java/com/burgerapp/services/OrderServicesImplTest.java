package com.burgerapp.services;

import com.burgerapp.domain.*;
import com.burgerapp.exception.BadRequestException;
import com.burgerapp.helpers.TestDataProviders;
import com.burgerapp.mapper.OrderMapper;
import com.burgerapp.mapper.OrderMapperTest;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import com.burgerapp.model.OrderDTO;
import com.burgerapp.model.OrderDataDTO;
import com.burgerapp.repositories.OrderRepository;
import com.burgerapp.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class OrderServicesImplTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    UserRepository userRepository;

    private OrderService underTest;

    private Order order;

    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new OrderServiceImpl(OrderMapper.INSTANCE,orderRepository,userRepository);
        user = TestDataProviders.createTestUser(true);
        order = TestDataProviders.createOrderEntity(true);
        order.setUser(user);

    }

//    @Configuration
//    @ComponentScan("com.burgerapp.")
//    public static class SpringConfig {
//
//    }

    @Test
    public void getAllOrders() {

        //when
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<OrderDTO> orderDTO = underTest.getAllOrders();

        assertEquals(1,orderDTO.size());
    }

    @Test
    public void createOrder() {
        //the burger being created
        IngredientDTO ig1 = new IngredientDTO("Onion","onion",2L,null);

        IngredientDTO ig2 = new IngredientDTO("Chicken","chicken",2L,null);
        IngredientDTO ig3 = new IngredientDTO("Bacon","chicken",2L,null);

        OrderDataDTO orderDataDTO = new OrderDataDTO(TestDataProviders.NAME_1,TestDataProviders.STREET,TestDataProviders.ZIP_CODE,TestDataProviders.COUNTRY,TestDataProviders.TEST_EMAIL_1,DeliveryMethod.FASTEST);


        BurgerDTO burgerDTO = new BurgerDTO(new HashSet<>(Arrays.asList(ig1,ig2,ig3)),TestDataProviders.PRICE);
        OrderDTO inputDTO = new OrderDTO(null,burgerDTO,orderDataDTO,null,false);

        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(optionalUser);

        //when
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = underTest.createOrder(inputDTO,"user1");

        verify(userRepository).findByUsername(anyString());
        assertEquals(inputDTO.getArchived(),result.getArchived());
        assertThat(result.getUserId(),is(1L));
        assertEquals(inputDTO.getBurger().getIngredients().size(),result.getBurger().getIngredients().size());
//        assertThat(result.getOrderData().getCountry(),TestDataProviders.COUNTRY);

    }

    @Test
    public void updateOrderById() {


        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        BDDMockito.given(orderRepository.save(any(Order.class))).willReturn(order);

        OrderDTO putData = new OrderDTO(order.getId(),null,null,null,true);
        OrderDTO result = underTest.updateOrderById(order.getId(),putData);
        System.out.println("GET ARCHIVED:"+result.getArchived());
        assertTrue(result.getArchived());
    }

    @Test
    public void getOrderById(){

        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(order));

        OrderDTO result = underTest.getOrder(any(Long.class));

        verify(orderRepository, times(1)).findById(any(Long.class));
        assertEquals(DeliveryMethod.FASTEST,result.getOrderData().getDeliveryMethod());
        assertEquals(TestDataProviders.TEST_EMAIL_1,result.getOrderData().getEmail());
    }

    @Test
    public void getOrdersByUserId() {
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByUserIdAndUnArchived(any(Long.class))).thenReturn(orders);

        List<OrderDTO> result = underTest.getOrdersByUserId("1");
        verify(orderRepository, times(1)).findByUserIdAndUnArchived(any(Long.class));
        assertEquals(1,result.size());

    }

    @Test(expected = BadRequestException.class)
    public void getOrdersByUserId_throwException() {
        List<OrderDTO> result = underTest.getOrdersByUserId("1L");
        verify(orderRepository, times(0)).findByUserIdAndUnArchived(any(Long.class));

    }

}
