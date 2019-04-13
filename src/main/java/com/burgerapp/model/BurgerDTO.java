package com.burgerapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BurgerDTO {
//    @NonNull
    private Set<IngredientDTO> ingredients;
    private Double price;
}
