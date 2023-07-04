package com.mykola.shoppingcart.service;

import com.mykola.shoppingcart.dao.ShoppingCartDao;
import com.mykola.shoppingcart.dao.ShoppingCartItemDao;
import com.mykola.shoppingcart.entities.Product;
import com.mykola.shoppingcart.entities.ShoppingCart;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import com.mykola.shoppingcart.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartDao shoppingCartDao;

    @Mock
    private ShoppingCartItemDao shoppingCartItemDao;


    private ShoppingCartService shoppingCartService;
    @Mock
    private ShoppingCartItemService shoppingCartItemService;

    public ShoppingCartServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.shoppingCartService = new ShoppingCartService(shoppingCartDao, shoppingCartItemService);
    }

    @Test
    @DisplayName("Call to method findById in Dao and return ShoppingCart")
    void findByIdShouldCallFindByIdInDaoAndReturnShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();

        when(shoppingCartDao.findById(1l)).thenReturn(Optional.of(shoppingCart));
        shoppingCartService.findById(1l);
        verify(shoppingCartDao).findById(1l);
    }

    @Test
    @DisplayName("Call to method findById in Dao and return message when pass does not exist id")
    void findByIdShouldCallFindByIdInDaoAndReturnNotFoundException() {
        ShoppingCart shoppingCart = new ShoppingCart();

        when(shoppingCartDao.findById(1l)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> shoppingCartService.findById(1l));
        verify(shoppingCartDao).findById(1l);
    }

    @Test
    @DisplayName("Call to method deleteItemFromCart in service")
    void emptyTrashShouldCallMethodDeleteItemFromCartInService() {
        ShoppingCartItem item = new ShoppingCartItem().builder()
                .quantity(2)
                .product(new Product().builder().title("PRODUCT").build())
                .build();
        List<ShoppingCartItem> itemList = new ArrayList<>();
        itemList.add(item);
        ShoppingCart shoppingCart = new ShoppingCart().builder().itemList(itemList).build();

        doNothing().when(shoppingCartItemService).deleteItemFromCart(item);
        shoppingCartService.emptyTrash(shoppingCart);
        verify(shoppingCartItemService).deleteItemFromCart(item);
    }
}
