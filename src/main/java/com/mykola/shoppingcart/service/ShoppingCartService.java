package com.mykola.shoppingcart.service;

import com.mykola.shoppingcart.dao.ShoppingCartDao;
import com.mykola.shoppingcart.entities.ShoppingCart;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import com.mykola.shoppingcart.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Shopping-cart Service
 */
@Service
@AllArgsConstructor
public class ShoppingCartService {
    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private ShoppingCartItemService shoppingCartItemService;

    /**
     * Get the Shopping-cart from Db
     *
     * @param id - Shopping-cart id
     * @return the found Shopping-cart
     */
    public ShoppingCart findById(Long id) {
        NotFoundException notFoundException = new NotFoundException("Shopping cart isn't found by id = " + id);
        ShoppingCart shoppingCart = shoppingCartDao.findById(id).orElse(null);
        if (shoppingCart == null) throw notFoundException;
        return shoppingCart;
    }

    /**
     * Empty the Shopping-cart from Db
     *
     * @param shoppingCart - the Shopping-cart
     */
    public void emptyTrash(ShoppingCart shoppingCart) {
        for (ShoppingCartItem item : shoppingCart.getItemList()) {
            shoppingCartItemService.deleteItemFromCart(item);
        }
        shoppingCart.getItemList().clear();
    }
}
