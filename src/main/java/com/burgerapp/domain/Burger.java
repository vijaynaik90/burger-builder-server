package com.burgerapp.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Burger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "burger",orphanRemoval = true)
    private Set<Ingredient> ingredients = new HashSet<>();

    @Column(name="total_price")
    private Double totalPrice;
//    TODO: maybe add image later.
//    @Lob
//    private Byte[] image;

    //this is needed for maintaing the one to many relationship
    public Burger addIngredient(Ingredient ingredient) {
        ingredient.setBurger(this);
        this.ingredients.add(ingredient);
        return this;
    }
}
