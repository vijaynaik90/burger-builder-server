package com.burgerapp.mapper;


import com.burgerapp.domain.Order;
import com.burgerapp.domain.User;
import com.burgerapp.helpers.TestDataProviders;
import com.burgerapp.model.OrderDTO;
import org.junit.Test;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class OrderMapperTest {

    OrderMapper underTest = OrderMapper.INSTANCE;

    @Test
    public void toOrderDTO () {
        //given an order test whether its converted correctly to orderDTO.
        Order order = TestDataProviders.createOrderEntity(true);
        User user = TestDataProviders.createTestUser(true);
        order.setUser(user);
        OrderDTO result = underTest.toOrderDTO(order);
        assertThat(order.getUser().getId(),equalTo(result.getUserId()));
        assertEquals(order.getBurger().getTotalPrice(),result.getBurger().getPrice());
        assertThat(result.getBurger(),hasProperty("ingredients",hasSize(3)));
    }

    @Test
    public void toOrder () {
        //given orderDto. convert it to order correctly.
        OrderDTO orderDTO = TestDataProviders.createOrderDTO(true);
        Order result = underTest.toOrder(orderDTO);
        assertFalse(result.getArchived());
        assertNull(result.getUser());
        assertEquals(orderDTO.getOrderData().getCountry(),result.getOrderData().getCountry());
        assertThat(result.getBurger().getIngredients(),hasSize(3));

    }
}
