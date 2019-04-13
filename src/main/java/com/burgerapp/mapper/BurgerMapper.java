package com.burgerapp.mapper;

import com.burgerapp.domain.Burger;
import com.burgerapp.domain.Ingredient;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

@Mapper
public interface BurgerMapper {

    BurgerMapper INSTANCE = Mappers.getMapper(BurgerMapper.class);


    //convert Burger Entity to Burger DTO

    @Mappings({
            @Mapping(source = "ingredients",target = "ingredients"),
            @Mapping(source="totalPrice",target="price")
    })
    BurgerDTO toBurgerDTO(Burger burger);

    @Mapping(target = "burger_id",source="burger.id")
    IngredientDTO toIngredientDTO(Ingredient source);

    default Burger toBurger(BurgerDTO burgerDTO){
        if ( burgerDTO == null ) {
            return null;
        }

        Burger burger = new Burger();

        Set<IngredientDTO> dtoset = burgerDTO.getIngredients();
        if ( dtoset == null ) {
            return null;
        }
        Set<Ingredient> set1 = new HashSet<Ingredient>( Math.max( (int) ( dtoset.size() / .75f ) + 1, 16 ) );
        for ( IngredientDTO ingredientDTO : dtoset ) {
            if ( ingredientDTO == null ) {
                return null;
            }
            Ingredient ingredient = new Ingredient();
            ingredient.setLabel(ingredientDTO.getLabel());
            ingredient.setName( ingredientDTO.getName() );
            ingredient.setQuantity( ingredientDTO.getQuantity());
            burger.addIngredient(ingredient);
        }
        burger.setTotalPrice(burgerDTO.getPrice());

        return burger;
    }

}
