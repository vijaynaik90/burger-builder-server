package com.burgerapp.services;

import com.burgerapp.domain.Burger;
import com.burgerapp.domain.Ingredient;
import com.burgerapp.helpers.TestDataProviders;
import com.burgerapp.mapper.BurgerMapper;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import com.burgerapp.repositories.BurgerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class BurgerServiceImplTest {

    @Mock
    private BurgerRepository burgerRepository;

    BurgerService underTest;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new BurgerServiceImpl(BurgerMapper.INSTANCE,burgerRepository);
    }

    @Test
    public void getAllBurgers(){
//        Ingredient ingredient1 = new Ingredient();
//        ingredient1.setName("Salad");
//        ingredient1.setQuantity(1L);
//
//        Ingredient ingredient2 = new Ingredient();
//        ingredient2.setName("Cheese");
//        ingredient2.setQuantity(2L);

        Burger b = TestDataProviders.createBurgerEntity(Arrays.asList("Chicken","Cheese"),2L,true);

        when(burgerRepository.findAll()).thenReturn(Arrays.asList(b));

        List<BurgerDTO> list = underTest.getAllBurgers();

        assertEquals(1,list.size());
        assertEquals(2,list.get(0).getIngredients().size());

    }

    @Test
    public void createBurger() {
        Burger b = new Burger();
        b.setId(1L);
        List<String> ingredients= Arrays.asList("Chicken","Cheese");
        b = createIngredientsandAddToBurger(ingredients,b,false);

        //the burger being created
        IngredientDTO ig1 = new IngredientDTO("onion","Onion",2L,null);
        IngredientDTO ig2 = new IngredientDTO("chicken","Chicken",1L,null);

        BurgerDTO burgerDTO = new BurgerDTO(new HashSet<>(Arrays.asList(ig1,ig2)),7.2);
        when(burgerRepository.save(any(Burger.class))).thenReturn(b);

        BurgerDTO result = underTest.createBurger(burgerDTO);

        assertEquals(b.getIngredients().size(),result.getIngredients().size());
//        assertEquals(b.size(),result.getIngredients().size());

    }

//    @Test
//    public void initBurger() {
//        Burger b = new Burger();
//        b.setId(1L);
//        List<String> ingredients= Arrays.asList("Salad","Bacon","Cheese","Meat");
//        b = createIngredientsandAddToBurger(ingredients,b,true);
//
//        when(burgerRepository.save(any(Burger.class))).thenReturn(b);
//
//        BurgerDTO result = underTest.initBurger();
//
//        verify(burgerRepository, times(1)).save(any(Burger.class));
//        assertEquals(b.getIngredients().size(),result.getIngredients().size());
////        assertEquals(b.size(),result.getIngredients().size());
//
//    }


    private Burger createIngredientsandAddToBurger(List<String> igList,Burger burger,boolean isInit){
        if(igList == null)
            return null;
        Long id=1L;
        for(String str: igList) {
            Ingredient ingredient = new Ingredient();
            ingredient.setLabel(str);
            ingredient.setName(str.toLowerCase());
            ingredient.setQuantity(isInit ? 0L : 2L);
            ingredient.setId(id++);
            burger.addIngredient(ingredient);
        }
        return burger;
    }
}
