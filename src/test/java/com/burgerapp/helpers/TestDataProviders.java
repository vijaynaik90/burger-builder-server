package com.burgerapp.helpers;

import com.burgerapp.domain.*;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import com.burgerapp.model.OrderDTO;
import com.burgerapp.model.OrderDataDTO;

import java.util.*;

public class TestDataProviders {

    public static final List<String> INGREDIENT_LIST = Collections.unmodifiableList(Arrays.asList("Chicken","Bacon","Onion"));
    public static final String TEST_EMAIL_1 = "test1@test.com";
    public static final String TEST_EMAIL_2 = "test2@test.com";
    public static final String NAME_1="VIJAY NAIK";
    public static final String NAME_2="NIDHI NAIK";
    public static final String STREET="123 Test Street";
    public static final String ZIP_CODE="12345";
    public static final String COUNTRY="USA";
    public static final Double PRICE=9.1;

    //// ************START Entity Creation *************/////

    public static OrderData createOrderData(Boolean withId){
        OrderData od = new OrderData(withId ? 1L: null,NAME_1,STREET,ZIP_CODE,COUNTRY,TEST_EMAIL_1,DeliveryMethod.FASTEST);
        return od;
    }

    public static OrderDataDTO createOrderDataDTO(){
        OrderDataDTO od = new OrderDataDTO(NAME_1,STREET,ZIP_CODE,COUNTRY,TEST_EMAIL_1,DeliveryMethod.FASTEST);
        return od;
    }

    public static Burger createBurgerEntity(List<String> igNames, Long quantity,Boolean withId) {
        if(igNames == null || igNames.size() <= 0)
            return null;
        Burger b = new Burger();
        if (withId)
            b.setId(1L);
        Ingredient ingredient;
        Long id=1L;
        for(String str: igNames){
            ingredient = new Ingredient();
            if (withId) {
                ingredient.setId(id++);
            }
            ingredient.setLabel(str);
            ingredient.setName(str.toLowerCase());
            ingredient.setQuantity(quantity);
            if(b!=null){
                b.addIngredient(ingredient);
            }

        }
        return b;
    }

    public static User createTestUser(Boolean withId) {
        User user = new User(NAME_1,"user1",TEST_EMAIL_1);
        if (withId)
            user.setId(1L);
        return user;
    }

    public static Order createOrderEntity (Boolean withId) {
        Order order = new Order();
        if(withId){
            order.setId(1L);
        }
        order.setBurger(createBurgerEntity(INGREDIENT_LIST,2L,withId));
        order.setOrderData(createOrderData(withId));
        order.setArchived(false);
        return order;
    }

    //// ************END Entity Creation *************/////

    public static OrderDTO createOrderDTO (Boolean withId) {
        OrderDTO orderDto = new OrderDTO();
        if(withId){
            orderDto.setId(1L);
        }
        orderDto.setBurger(createBurgerDTO(INGREDIENT_LIST,100L));
        orderDto.setOrderData(createOrderDataDTO());
        orderDto.setArchived(false);
        orderDto.setUserId(1L);
        return orderDto;
    }

    public static BurgerDTO createBurgerDTO(List<String> igNames, Long quantity) {
        if(igNames == null || igNames.size() <= 0)
            return null;
        BurgerDTO b = new BurgerDTO();
        Set<IngredientDTO> dtoset = new HashSet<IngredientDTO>( Math.max( (int) ( igNames.size() / .75f ) + 1, 16 ) );
        IngredientDTO ingredientDto;
        for(String str: igNames){
            ingredientDto = new IngredientDTO();
            ingredientDto.setLabel(str);
            ingredientDto.setName(str.toLowerCase());
            ingredientDto.setQuantity(quantity);
            dtoset.add(ingredientDto);
        }
        b.setIngredients(dtoset);
        b.setPrice(PRICE);
        return b;
    }



//    public static User createUserForIT() {
//        User user = new User(NAME_1,"user1",TEST_EMAIL_1);
//
//        return user;
//    }
}
