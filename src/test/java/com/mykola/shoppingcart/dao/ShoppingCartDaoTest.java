package com.mykola.shoppingcart.dao;

import com.mykola.shoppingcart.configuration.ApplicationConfig;
import com.mykola.shoppingcart.entities.ShoppingCart;
import com.mykola.shoppingcart.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Import(ApplicationConfig.class)
class ShoppingCartDaoTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    private static boolean isInsertDataDone = false;
    private static User currentUser = new User().builder()
            .username("User 1")
            .email("user1@gmail.com")
            .password("1234")
            .build();
    private static ShoppingCart shoppingCart = new ShoppingCart();

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
            session.save(currentUser);
            session.getTransaction().commit();

            currentUser.setShoppingCart(shoppingCart);
            shoppingCart.setUser(currentUser);
            session.beginTransaction();
            session.save(shoppingCart);
            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Save new shopping-cart")
    void saveShouldSaveNewShoppingCart() {
        ShoppingCart result = shoppingCartDao.save(new ShoppingCart());

        assertNotEquals(result.getId(), null);
    }

    @Test
    @DisplayName("Find shopping-cart by id=1")
    void findByIdShouldReturnShoppingCartBy1Id() {
        Optional<ShoppingCart> result = shoppingCartDao.findById(shoppingCart.getId());
        if (result.isPresent()) {
            assertEquals(result.get().getUser().getUsername(), currentUser.getUsername());
        } else fail("ShoppingCart doesn't exist");
    }

    @Test
    @DisplayName("Delete shopping-cart")
    void deleteShouldDeleteShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        Long idNewCart = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            idNewCart = (long) session.save(cart);
            session.getTransaction().commit();
        }

        shoppingCartDao.delete(cart);

        ShoppingCart shoppingCartFromBd = null;
        try (Session session = sessionFactory.openSession()) {
            shoppingCartFromBd = session.get(ShoppingCart.class, idNewCart);
        }
        assertEquals(shoppingCartFromBd, null);
    }
}
