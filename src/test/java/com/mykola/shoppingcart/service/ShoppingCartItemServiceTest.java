package com.mykola.shoppingcart.service;

import com.mykola.shoppingcart.dao.ShoppingCartItemDao;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

class ShoppingCartItemServiceTest {
    @Mock
    private ShoppingCartItemDao shoppingCartItemDao;

    private ShoppingCartItemService shoppingCartItemService;

    public ShoppingCartItemServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.shoppingCartItemService = new ShoppingCartItemService(shoppingCartItemDao);
    }

    @Test
    @DisplayName("Call to method SAVE in Dao")
    void addItemToCartShouldCallSaveInDao() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem();
        when(shoppingCartItemDao.save(item)).thenReturn(item);

        shoppingCartItemService.addItemToCart(item);
        verify(shoppingCartItemDao).save(item);
    }

    @Test
    @DisplayName("Call to method update in Dao")
    void changeQuantityShouldCallUpdateInDao() {
        ShoppingCartItem item = new ShoppingCartItem();
        doNothing().when(shoppingCartItemDao).update(item);

        shoppingCartItemService.changeQuantity(item);
        verify(shoppingCartItemDao).update(item);
    }

    @Test
    @DisplayName("Call to method delete in Dao")
    void deleteItemFromCart() {
        ShoppingCartItem item = new ShoppingCartItem();
        doNothing().when(shoppingCartItemDao).delete(item);

        shoppingCartItemService.deleteItemFromCart(item);
        verify(shoppingCartItemDao).delete(item);
    }

}
