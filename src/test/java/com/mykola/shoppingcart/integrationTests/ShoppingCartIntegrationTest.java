package com.mykola.shoppingcart.integrationTests;

import com.mykola.shoppingcart.api.controllers.ShoppingCartController;
import com.mykola.shoppingcart.api.dto.ShoppingCartDto;
import com.mykola.shoppingcart.api.dto.UserDetailDto;
import com.mykola.shoppingcart.configuration.ApplicationConfig;
import com.mykola.shoppingcart.entities.Product;
import com.mykola.shoppingcart.entities.ShoppingCart;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import com.mykola.shoppingcart.entities.User;
import com.mykola.shoppingcart.security.service.CurrentUserContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@Import(ApplicationConfig.class)
public class ShoppingCartIntegrationTest {
    @Autowired
    private SessionFactory sessionFactory;

    @MockBean
    private CurrentUserContext currentUserContext;

    @Autowired
    private ShoppingCartController cartController;

    private static boolean isInsertDataDone = false;
    private static Product product1 = new Product().builder()
            .title("Table")
            .price(new BigDecimal("119.99"))
            .build();
    private static Product product2 = new Product().builder()
            .title("Laptop")
            .price(new BigDecimal("688.16"))
            .build();
    private static Product product3 = new Product().builder()
            .title("Cup")
            .price(new BigDecimal("12.99"))
            .build();
    private static User currentUser = new User().builder()
            .username("Current User")
            .email("cur-user@gmail.com")
            .password("1234")
            .build();
    private static ShoppingCart shoppingCart = new ShoppingCart();
    private static ShoppingCartItem item1 = new ShoppingCartItem().builder()
            .product(product1)
            .shoppingCart(shoppingCart)
            .quantity(2)
            .build();

    @BeforeEach
    public void insertData() {
        if(!isInsertDataDone) {
            addDataInDb();
            isInsertDataDone = true;
        }
    }

    private void addDataInDb() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(product1);
            session.save(product2);
            session.save(product3);
            session.getTransaction().commit();

            session.beginTransaction();
            session.save(currentUser);
            session.getTransaction().commit();

            currentUser.setShoppingCart(shoppingCart);
            shoppingCart.setUser(currentUser);
            session.beginTransaction();
            session.save(shoppingCart);
            session.getTransaction().commit();

            session.beginTransaction();
            session.save(item1);
            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Get shopping-cart from Db through controller")
    public void getShoppingCartShouldGetShoppingCartFromDb() {
        when(currentUserContext.getCurrentUser()).thenReturn(UserDetailDto.fromUser(currentUser));

        ResponseEntity<ShoppingCartDto> response = cartController.getShoppingCart();
        assertEquals(response.getBody().getId(), shoppingCart.getId());
    }

    @Test
    @DisplayName("Add product to the shopping cart and save Db through controller")
    public void addItemToCartShouldAddItemToDb() {
        when(currentUserContext.getCurrentUser()).thenReturn(UserDetailDto.fromUser(currentUser));

        ShoppingCartItem newItem = new ShoppingCartItem().builder()
                .product(product2)
                .shoppingCart(shoppingCart)
                .quantity(34)
                .build();
        ShoppingCart shopCartBd = null;
        try (Session session = sessionFactory.openSession()) {
            shopCartBd = session.get(ShoppingCart.class, currentUser.getShoppingCart().getId());
        }
        assertEquals(shopCartBd.getItemList().size(), 1);

        cartController.addItemToCart(newItem);
        try (Session session = sessionFactory.openSession()) {
            shopCartBd = session.get(ShoppingCart.class, currentUser.getShoppingCart().getId());
        }
        assertNotEquals(newItem.getId(), null);
        assertEquals(shopCartBd.getItemList().size(), 2);
    }

    @Test
    @DisplayName("Delete product in shopping cart and save it in Db")
    public void deleteItemShouldReturnOk() {
        when(currentUserContext.getCurrentUser()).thenReturn(UserDetailDto.fromUser(currentUser));

        ShoppingCartItem itemBd = null;
        try (Session session = sessionFactory.openSession()) {
            itemBd = session.get(ShoppingCartItem.class, item1.getId());
        }
        assertNotEquals(itemBd, null);

        cartController.deleteItem(item1);
        try (Session session = sessionFactory.openSession()) {
            itemBd = session.get(ShoppingCartItem.class, item1.getId());
        }
        assertEquals(itemBd, null);
    }

    @Test
    @DisplayName("changeQuantity should change quantity in ShoppingCartitem and save it in Db")
    public void changeQuantityShouldChangeQuantityInDb() {
        when(currentUserContext.getCurrentUser()).thenReturn(UserDetailDto.fromUser(currentUser));
        ShoppingCartItem newItem = new ShoppingCartItem().builder()
                .quantity(5)
                .shoppingCart(shoppingCart)
                .product(product3)
                .build();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(newItem);
            session.getTransaction().commit();
        }

        newItem.setQuantity(32);
        cartController.changeQuantity(newItem);
        try (Session session = sessionFactory.openSession()) {
            newItem = session.get(ShoppingCartItem.class, newItem.getId());
        }
        assertEquals(newItem.getQuantity(), 32);
    }
}
