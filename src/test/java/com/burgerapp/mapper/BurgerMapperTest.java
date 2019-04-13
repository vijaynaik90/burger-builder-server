package com.burgerapp.mapper;

import com.burgerapp.domain.Burger;
import com.burgerapp.domain.Ingredient;
import com.burgerapp.helpers.TestDataProviders;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BurgerMapperTest {

    public static final String BACON = "Bacon";
    public static final Long AMOUNT1 = 2L;

    public static final String SALAD = "Salad";
    public static final Long AMOUNT2 = 1L;
    public static Double PRICE = 9.5;

    BurgerMapper underTest = BurgerMapper.INSTANCE;

    @Test
    public void burgerToBurgerDTO() {

        Burger burger = TestDataProviders.createBurgerEntity(Arrays.asList("Bacon","Salad"),2L,true);
//        Burger burger =  new Burger();
//        Ingredient ingredient1 = new Ingredient();
//        ingredient1.setName(BACON);
//        ingredient1.setQuantity(AMOUNT1);
//        ingredient1.setId(1L);
//
//        Ingredient ingredient2 = new Ingredient();
//        ingredient2.setName(SALAD);
//        ingredient2.setQuantity(AMOUNT2);
//        ingredient2.setId(2L);
//
//        burger.getIngredients().add(ingredient1);
//        burger.getIngredients().add(ingredient2);

        BurgerDTO burgerDTO = underTest.toBurgerDTO(burger);

//        System.out.println(burgerDTO.toString());
        assertEquals(2,burgerDTO.getIngredients().size());
        assertThat(burgerDTO.getIngredients().iterator().next().getBurger_id(),equalTo(1L));


    }

    @Test
    public void toBurger() {
        IngredientDTO ingredient1 = new IngredientDTO();
        ingredient1.setName(BACON);
        ingredient1.setQuantity(AMOUNT1);

        IngredientDTO ingredient2 = new IngredientDTO();
        ingredient2.setName(SALAD);
        ingredient2.setQuantity(AMOUNT2);

        BurgerDTO burgerDTO =  new BurgerDTO(new HashSet<>(Arrays.asList(ingredient1,ingredient2)),PRICE);

        Burger burger = underTest.toBurger(burgerDTO);

        System.out.println(burger.toString());
        assertEquals(2,burgerDTO.getIngredients().size());
        assertThat(PRICE,Matchers.equalTo(burger.getTotalPrice()));
        assertThat(burger.getIngredients(),hasSize(2));
    }



}
