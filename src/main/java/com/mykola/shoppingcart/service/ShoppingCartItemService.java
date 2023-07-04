package com.mykola.shoppingcart.service;

import com.mykola.shoppingcart.dao.ShoppingCartItemDao;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Shopping-cart item Service
 */
@Service
@AllArgsConstructor
public class ShoppingCartItemService {
    @Autowired
    private ShoppingCartItemDao shoppingCartItemDao;

    /**
     * Add the Shopping-cart item to Db
     *
     * @param shoppingCartItem - new item in the Shopping-cart
     */
    public void addItemToCart(ShoppingCartItem shoppingCartItem) {
        shoppingCartItemDao.save(shoppingCartItem);
    }

    /**
     * Change quantity the Shopping-cart item in Db
     *
     * @param shoppingCartItem - the item with a different quantity
     */
    public void changeQuantity(ShoppingCartItem shoppingCartItem) {
        shoppingCartItemDao.update(shoppingCartItem);
    }

    /**
     * Delete the Shopping-cart item from Db
     *
     * @param shoppingCartItem - the Shopping-cart item
     */
    public void deleteItemFromCart(ShoppingCartItem shoppingCartItem) {
        shoppingCartItemDao.delete(shoppingCartItem);
    }
}
