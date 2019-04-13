package com.burgerapp.services;

import com.burgerapp.model.BurgerDTO;

import java.util.List;

public interface BurgerService {

    // create a burger with no ingredients.
    BurgerDTO createBurger(BurgerDTO burgerDTO);

    List<BurgerDTO> getAllBurgers();

    BurgerDTO initBurger();
}
