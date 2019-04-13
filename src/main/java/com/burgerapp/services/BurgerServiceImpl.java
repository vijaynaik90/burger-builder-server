package com.burgerapp.services;

import com.burgerapp.domain.Burger;
import com.burgerapp.exception.ResourceNotFoundException;
import com.burgerapp.mapper.BurgerMapper;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import com.burgerapp.repositories.BurgerRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("burgerService")
public class BurgerServiceImpl implements BurgerService {

    public static List<String> INIT_BURGER_LIST= Arrays.asList("Salad","Bacon","Cheese","Meat");
    public static Long INIT_ID=1L;
    private final BurgerMapper burgerMapper;

    private final BurgerRepository burgerRepository;

    public BurgerServiceImpl(BurgerMapper burgerMapper, BurgerRepository burgerRepository) {
        this.burgerMapper = burgerMapper;
        this.burgerRepository = burgerRepository;
    }

    //TODO: need to create init burger method

    @Override
    public BurgerDTO createBurger(BurgerDTO postData) {
    // Create a burger having all ingredients as 0
        return saveAndReturnBurgerDTO(postData);
    }

    @Override
    public List<BurgerDTO> getAllBurgers(){
        List<Burger> burgerList = burgerRepository.findAll();
                return burgerList.stream()
                            .map(burgerMapper::toBurgerDTO)
                            .collect(Collectors.toList());
    }

    @Override
    public BurgerDTO initBurger(){
//        List<IngredientDTO> list = createInitIngredients(INIT_BURGER_LIST);
//        BurgerDTO burgerDTO = new BurgerDTO(new HashSet<>(list),5.0);

        Optional<Burger> burger = burgerRepository.findById(INIT_ID);

        return burger.map(burgerMapper::toBurgerDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot find init burger "));
    }

    private List<IngredientDTO> createInitIngredients(List<String> igNames) {
        if(igNames == null || igNames.size() <= 0)
            return null;
        List<IngredientDTO> igList = new ArrayList<>();
        IngredientDTO ingredientDTO = null;
        for(String str: igNames){
            ingredientDTO = new IngredientDTO(str,str.toLowerCase(),0L,null);
            igList.add(ingredientDTO);
        }
        return igList;
    }

    private BurgerDTO saveAndReturnBurgerDTO(BurgerDTO postData){
        Burger detachedBurger = burgerMapper.toBurger(postData);
        Burger savedBurger = burgerRepository.save(detachedBurger);

        return burgerMapper.toBurgerDTO(savedBurger);
    }
}
