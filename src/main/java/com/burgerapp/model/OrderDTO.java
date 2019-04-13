package com.burgerapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;

//    @NonNull
    private BurgerDTO burger;

    @JsonProperty("order_data")
    private OrderDataDTO orderData;

    @JsonProperty("user_id")
    private Long userId;

    private Boolean archived;
}
