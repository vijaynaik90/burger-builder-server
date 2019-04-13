package com.burgerapp.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// Every order should be for one burger.so theres a one to one relationship between them.
@Entity
@Table(name="orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) @JoinColumn(name="burger_id", updatable = true)
    private Burger burger;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) @JoinColumn(name="order_data_id", updatable = true)
    private OrderData orderData;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    private Boolean archived = false;


}
