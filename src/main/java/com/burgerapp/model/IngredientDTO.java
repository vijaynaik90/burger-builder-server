package com.burgerapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    @NonNull
    private String name;
    @NonNull
    private String label;
    @NonNull
    private Long quantity;
    private Long burger_id;
}
