package com.mykola.shoppingcart.api.dto;

import com.mykola.shoppingcart.entities.Product;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Shopping Cart Item Dto
 */
@AllArgsConstructor
@Data
public class ShoppingCartItemDto {
    private Long id;
    private int quantity;
    private Product product;
    private BigDecimal totalPrice;

    /**
     * Get Shopping Cart Item Dto with total price from shopping-cart item
     *
     * @param item - one item in the shopping-cart
     * @return Shopping Cart Item Dto with total Price
     */
    public static ShoppingCartItemDto fromShoppingCartItem(ShoppingCartItem item) {
        return new ShoppingCartItemDto(
                item.getId(),
                item.getQuantity(),
                item.getProduct(),
                item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()))
        );
    }
}
