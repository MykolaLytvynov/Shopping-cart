package com.mykola.shoppingcart.dao;

import com.mykola.shoppingcart.entities.ShoppingCart;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Shopping-cart Dao
 */
@Component
@AllArgsConstructor
public class ShoppingCartDao implements CrudOperations<ShoppingCart, Long>{
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Save the Shopping-cart to Db
     *
     * @param shoppingCart
     * @return saved Shopping-cart
     */
    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        Long id;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            id = (Long) session.save(shoppingCart);
            session.getTransaction().commit();
        }
        shoppingCart.setId(id);
        return shoppingCart;
    }

    /**
     * Get the Shopping-cart from Db
     *
     * @param id - Shopping-cart id
     * @return the found Shopping-cart
     */
    @Override
    public Optional<ShoppingCart> findById(Long id) {
        ShoppingCart result;
        try (Session session = sessionFactory.openSession()) {
            result = session.get(ShoppingCart.class, id);
        }
        return ofNullable(result);
    }

    /**
     * Delete the Shopping-cart from Db
     *
     * @param shoppingCart - the Shopping-cart
     */
    @Override
    public void delete(ShoppingCart shoppingCart) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(shoppingCart);
            session.getTransaction().commit();
        }
    }
}
