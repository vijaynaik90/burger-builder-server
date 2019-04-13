package com.burgerapp.controller.v1;

import com.burgerapp.config.WithMockBasicAuth;
import com.burgerapp.domain.User;
import com.burgerapp.exception.ResourceNotFoundException;
import com.burgerapp.helpers.TestDataProviders;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import com.burgerapp.model.OrderDTO;
import com.burgerapp.model.OrderDataDTO;
import com.burgerapp.security.UserPrincipal;
import com.burgerapp.services.OrderService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends AbstractRestControllerTest {

    public static final String TEST_EMAIL = "test@Test.com";
    public static final String ORDER_URL = "/api/v1/orders";
    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController underTest;

    User user;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
        user = createDummyUser();
    }

    @Test
    public void getOrders() throws Exception {
        OrderDTO order1 = new OrderDTO(1L,new BurgerDTO(),new OrderDataDTO(),1L,false);
        OrderDTO order2 = new OrderDTO(2L,new BurgerDTO(),new OrderDataDTO(),1L,false);

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1,order2));

        mockMvc.perform(get(ORDER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders",hasSize(2)));
    }


    @Test
//    @WithMockUser(username = "spring",roles = {"USER"})
    public void testCreateOrder() throws Exception{

        BurgerDTO burgerDTO = createBurgerDTO(TestDataProviders.INGREDIENT_LIST);
        OrderDTO inputDTO = new OrderDTO(null,burgerDTO,new OrderDataDTO(),user.getId(),false);

        List<IngredientDTO> finalList =  burgerDTO.getIngredients().stream().map(igDto -> {
            igDto.setBurger_id(1L);
            return igDto;
        }).collect(Collectors.toList());
        burgerDTO = new BurgerDTO(new HashSet<>(finalList),9.1);

        OrderDTO resultDTO = new OrderDTO(1L,burgerDTO,new OrderDataDTO(),1L,false);

        when(orderService.createOrder(inputDTO,user.getUsername())).thenReturn(resultDTO);



        mockMvc.perform(post(ORDER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputDTO))
                        .content(asJsonString(UserPrincipal.create(user))))
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.id",equalTo(1L)));


        verify(orderService,times(1)).createOrder(ArgumentMatchers.any(OrderDTO.class),anyString());

    }

    @Test
    public void getUnarchivedOrdersByUserId() throws Exception {
        OrderDTO order1 = new OrderDTO(1L,new BurgerDTO(),new OrderDataDTO(),1L,false);
        OrderDTO order2 = new OrderDTO(2L,new BurgerDTO(),new OrderDataDTO(),1L,false);

        when(orderService.getOrdersByUserId(anyString())).thenReturn(Arrays.asList(order1,order2));

        mockMvc.perform(get(ORDER_URL +"/user/"+1)
                        .param("archived","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders",Matchers.hasSize(2)));

        verify(orderService,times(0)).getArchivedOrdersByUserId(anyString());
        verify(orderService,times(1)).getOrdersByUserId(anyString());

    }

    @Test
    public void getArchivedOrdersByUserId() throws Exception {
        OrderDTO order1 = new OrderDTO(1L,new BurgerDTO(),new OrderDataDTO(),1L,true);
        OrderDTO order2 = new OrderDTO(2L,new BurgerDTO(),new OrderDataDTO(),1L,true);

        when(orderService.getArchivedOrdersByUserId(anyString())).thenReturn(Arrays.asList(order1,order2));

        mockMvc.perform(get(ORDER_URL+ "/user/"+1)
                .param("archived","true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders",Matchers.hasSize(2)));

        verify(orderService,times(1)).getArchivedOrdersByUserId(anyString());
        verify(orderService,times(0)).getOrdersByUserId(anyString());

    }

    @Test
    public void updateOrderById() throws Exception {
        OrderDTO putData = new OrderDTO();
        putData.setArchived(false);

        OrderDTO updatedOrder = getCompleteOrderDto();

        BDDMockito.given(orderService.updateOrderById(updatedOrder.getId(),putData)).willReturn(updatedOrder);

        mockMvc.perform(put(ORDER_URL + "/" + updatedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(putData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order_data.country",equalTo(TestDataProviders.COUNTRY)))
                .andExpect(jsonPath("$.archived",is(false)))
                .andExpect(jsonPath("$.burger.ingredients",hasSize(3)));

    }

    @Test
    public void testResourceNotFound() throws Exception {

        BDDMockito.given(orderService.updateOrderById(anyLong(),ArgumentMatchers.any(OrderDTO.class))).willThrow(new ResourceNotFoundException());

        mockMvc.perform(put(ORDER_URL + "/1212")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ArgumentMatchers.any(OrderDTO.class))))
                .andExpect(status().is4xxClientError());

    }

    private User createDummyUser(){
        User user = new User("Vijay","user1",TEST_EMAIL);
        return user;
    }

    private BurgerDTO createBurgerDTO(List<String> ingredients) {
        BurgerDTO burgerDTO = new BurgerDTO();
        burgerDTO.setPrice(TestDataProviders.PRICE);

        Set<IngredientDTO> set1 = new HashSet<>();
        for (String str: ingredients) {
            IngredientDTO ig = new IngredientDTO(str.toLowerCase(),str,2L,null);
            set1.add(ig);
        }
        burgerDTO.setIngredients(set1);
        return burgerDTO;
    }

    private OrderDTO getCompleteOrderDto() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setArchived(false);
        orderDTO.setId(1L);
        orderDTO.setUserId(1L);
        orderDTO.setBurger(createBurgerDTO(TestDataProviders.INGREDIENT_LIST));
        orderDTO.setOrderData(TestDataProviders.createOrderDataDTO());

        return orderDTO;
    }
}
