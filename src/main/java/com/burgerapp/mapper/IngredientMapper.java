package com.burgerapp.mapper;

import com.burgerapp.domain.Ingredient;
import com.burgerapp.model.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientMapper {
    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    IngredientDTO ingredientToIngredientDTO(Ingredient ingredient);
}
