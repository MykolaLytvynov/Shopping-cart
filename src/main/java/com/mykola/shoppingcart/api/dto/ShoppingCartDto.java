package com.mykola.shoppingcart.api.dto;

import com.mykola.shoppingcart.entities.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping Cart Dto
 */
@AllArgsConstructor
@Data
public class ShoppingCartDto {
    private Long id;
    private List<ShoppingCartItemDto> itemList;
    private BigDecimal sumOfAllGoods;

    /**
     * Get Shopping Cart Dto with sum of all items from shopping-cart
     *
     * @param shoppingCart
     * @return Shopping Cart Dto with sum of all items
     */
    public static ShoppingCartDto fromShoppingCart(ShoppingCart shoppingCart) {
        List<ShoppingCartItemDto> itemList = new ArrayList<>();
        shoppingCart.getItemList().forEach(item -> itemList.add(ShoppingCartItemDto.fromShoppingCartItem(item)));

        BigDecimal sumOfAllGoods = shoppingCart.getItemList().stream()
                .map(item -> ShoppingCartItemDto.fromShoppingCartItem(item).getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ShoppingCartDto(
                shoppingCart.getId(),
                itemList,
                sumOfAllGoods
        );
    }
}
