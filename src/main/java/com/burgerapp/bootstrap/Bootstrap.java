package com.burgerapp.bootstrap;

import com.burgerapp.domain.Burger;
import com.burgerapp.domain.Ingredient;
import com.burgerapp.repositories.BurgerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final BurgerRepository burgerRepository;

    public Bootstrap(BurgerRepository burgerRepository) {
        this.burgerRepository = burgerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        loadBurgers();
    }

    private void loadBurgers() {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Salad");
        ingredient1.setQuantity(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Cheese");
        ingredient2.setQuantity(2L);

        Burger b = new Burger();

        b.addIngredient(ingredient1);
        b.addIngredient(ingredient2);

        burgerRepository.save(b);

        System.out.println("Burger Count====>" + burgerRepository.count());
    }
}
