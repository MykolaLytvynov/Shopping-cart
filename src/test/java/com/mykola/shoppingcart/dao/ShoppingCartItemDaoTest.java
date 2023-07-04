package com.mykola.shoppingcart.dao;

import com.mykola.shoppingcart.configuration.ApplicationConfig;
import com.mykola.shoppingcart.entities.Product;
import com.mykola.shoppingcart.entities.ShoppingCart;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import com.mykola.shoppingcart.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(ApplicationConfig.class)
class ShoppingCartItemDaoTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ShoppingCartItemDao shoppingCartItemDao;

    private static boolean isInsertDataDone = false;
    private static Product product1 = new Product().builder()
            .title("Table")
            .price(new BigDecimal("119.99"))
            .build();
    private static Product product2 = new Product().builder()
            .title("Laptop")
            .price(new BigDecimal("688.16"))
            .build();
    private static User currentUser = new User().builder()
            .username("User 2")
            .email("user2@gmail.com")
            .password("1234")
            .build();
    private static ShoppingCart shoppingCart = new ShoppingCart();
    private static ShoppingCartItem item1 = new ShoppingCartItem().builder()
            .product(product1)
            .shoppingCart(shoppingCart)
            .quantity(2)
            .build();
    private static ShoppingCartItem item2 = new ShoppingCartItem().builder()
            .product(product2)
            .shoppingCart(shoppingCart)
            .quantity(5)
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
            session.save(item2);
            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Save new shopping-cart item")
    void saveShouldSaveNewShoppingCartItem() {
        ShoppingCartItem result = shoppingCartItemDao.save(new ShoppingCartItem());

        assertNotEquals(result.getId(), null);
    }

    @Test
    @DisplayName("Find shopping-cart item")
    void findByIdShouldReturnShoppingCartItem() {
        Optional<ShoppingCartItem> result = shoppingCartItemDao.findById(item1.getId());
        if (result.isPresent()) {
            assertEquals(result.get().getQuantity(), 2);
        } else fail("ShoppingCartItem doesn't exist");
    }

    @Test
    @DisplayName("Delete shopping-cart item")
    void deleteShouldDeleteShoppingCartItem() {
        ShoppingCartItem cartItem = new ShoppingCartItem();
        Long idNewItem = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            idNewItem = (long) session.save(cartItem);
            session.getTransaction().commit();
        }

        shoppingCartItemDao.delete(cartItem);

        ShoppingCartItem shoppingCartItemFromBd = null;
        try (Session session = sessionFactory.openSession()) {
            shoppingCartItemFromBd = session.get(ShoppingCartItem.class, idNewItem);
        }

        assertEquals(shoppingCartItemFromBd, null);
    }

    @Test
    @DisplayName("Update shopping-cart item")
    void updateShouldUpdateShoppingCartItem() {
        ShoppingCartItem cartItem;
        try (Session session = sessionFactory.openSession()) {
            cartItem = session.get(ShoppingCartItem.class, item2.getId());
        }
        assertEquals(cartItem.getQuantity(), 5);

        cartItem.setQuantity(3);
        shoppingCartItemDao.update(cartItem);
        assertEquals(cartItem.getQuantity(), 3);
    }
}
