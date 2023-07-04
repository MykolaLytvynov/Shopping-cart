package com.mykola.shoppingcart.entities;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shopping_cart_items")
public class ShoppingCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ShoppingCart shoppingCart;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
