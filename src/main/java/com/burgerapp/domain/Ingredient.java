package com.burgerapp.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="ingredient")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"burger"})
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    private String label;

    @NotNull
    @Size(max = 100)
    private String name;

    private Long quantity;

//    @OneToOne(fetch = FetchType.EAGER)
//    private UnitOfMeasure uom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "burger_id")
    private Burger burger;
}
