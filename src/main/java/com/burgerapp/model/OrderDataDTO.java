package com.burgerapp.model;

import com.burgerapp.domain.DeliveryMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDataDTO {

    @JsonProperty("name")
    private String customerName;
    private String street;
    @JsonProperty("zip_code")
    private String zipCode;
    private String country;
    private String email;

    @JsonProperty("delivery_method")
    private DeliveryMethod deliveryMethod;
}
