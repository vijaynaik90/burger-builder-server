package com.burgerapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "order_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String customerName;

    @NotBlank
    @Size(max = 50)
    private String street;

    @NotBlank
    @Size(max = 50)
    private String zipCode;

    @NotBlank
    @Size(max = 50)
    private String country;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;
}
