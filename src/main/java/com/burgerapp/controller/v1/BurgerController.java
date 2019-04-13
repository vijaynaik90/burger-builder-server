package com.burgerapp.controller.v1;

import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.BurgerListDTO;
import com.burgerapp.services.BurgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/burger")
public class BurgerController {

    @Autowired
    BurgerService burgerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BurgerDTO createBurger(@RequestBody BurgerDTO burgerDTO){
        return burgerService.createBurger(burgerDTO);
    }

    @GetMapping({"/init"})
    @ResponseStatus(HttpStatus.OK)
    public BurgerDTO initBurger(){
        return burgerService.initBurger();
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BurgerListDTO getAllBurgers() {
        List<BurgerDTO> list = burgerService.getAllBurgers();
        return new BurgerListDTO(list);
    }
}
