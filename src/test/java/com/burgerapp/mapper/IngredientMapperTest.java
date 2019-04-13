package com.burgerapp.mapper;

import com.burgerapp.domain.Ingredient;
import com.burgerapp.model.IngredientDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IngredientMapperTest {

    public static final String BACON = "Bacon";
    public static final Long AMOUNT = 2L;

    IngredientMapper underTest = IngredientMapper.INSTANCE;

    @Test
    public void ingredientToIngredientDTO () throws Exception {
     // ingredient entity should get converted to a Ingredient DTO entity.
        Ingredient ingredient = new Ingredient();
        ingredient.setName(BACON.toLowerCase());
        ingredient.setLabel(BACON);
        ingredient.setQuantity(AMOUNT);
        ingredient.setId(1L);

        IngredientDTO ingredientDTO = underTest.ingredientToIngredientDTO(ingredient);

        assertEquals(BACON.toLowerCase(),ingredientDTO.getName());
        assertEquals(AMOUNT,ingredientDTO.getQuantity());
    }

}
