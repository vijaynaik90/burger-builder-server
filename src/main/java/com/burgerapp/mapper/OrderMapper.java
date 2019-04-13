package com.burgerapp.mapper;

import com.burgerapp.domain.Burger;
import com.burgerapp.domain.Ingredient;
import com.burgerapp.domain.Order;
import com.burgerapp.model.BurgerDTO;
import com.burgerapp.model.IngredientDTO;
import com.burgerapp.model.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    BurgerMapper burgerMapper = BurgerMapper.INSTANCE;

    OrderDataMapper orderDataMapper = OrderDataMapper.INSTANCE;

    @Mapping(source = "user.id",target = "userId")
    OrderDTO toOrderDTO(Order order);

    @Mappings({
            @Mapping(source = "ingredients",target = "ingredients"),
            @Mapping(source="totalPrice",target="price")
    })
    BurgerDTO toBurgerDTO(Burger burger);

    @Mapping(target = "burger_id",source="burger.id")
    IngredientDTO toIngredientDTO(Ingredient source);

    default Order toOrder(OrderDTO orderDTO){
        if ( orderDTO == null ) {
            return null;
        }

        Order order = new Order();

        order.setId( orderDTO.getId() );
        order.setBurger( burgerMapper.toBurger(orderDTO.getBurger() ) );
        order.setOrderData(orderDataMapper.toOrderData(orderDTO.getOrderData()));
        if(orderDTO.getArchived() != null)
            order.setArchived(orderDTO.getArchived());

        return order;
    }
}
