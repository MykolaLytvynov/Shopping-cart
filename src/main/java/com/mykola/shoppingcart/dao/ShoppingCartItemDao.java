package com.mykola.shoppingcart.dao;

import com.mykola.shoppingcart.entities.ShoppingCartItem;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import static java.util.Optional.ofNullable;

/**
 * Shopping-cart item Dao
 */
@Component
@RequiredArgsConstructor
public class ShoppingCartItemDao implements CrudOperations<ShoppingCartItem, Long>{
    @Autowired
    private final SessionFactory sessionFactory;

    /**
     * Save the Shopping-cart item to Db
     *
     * @param item
     * @return saved item
     */
    @Override
    public ShoppingCartItem save(ShoppingCartItem item) {
        Long id;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            id = (Long) session.save(item);
            session.getTransaction().commit();
        }
        item.setId(id);
        return item;
    }

    /**
     * Get the Shopping-cart item from Db
     *
     * @param id - Shopping-cart item id
     * @return the found item
     */
    @Override
    public Optional<ShoppingCartItem> findById(Long id) {
        ShoppingCartItem result;
        try (Session session = sessionFactory.openSession()) {
            result = session.get(ShoppingCartItem.class, id);
        }
        return ofNullable(result);
    }

    /**
     * Delete the Shopping-cart item from Db
     *
     * @param item - the Shopping-cart item
     */
    @Override
    public void delete(ShoppingCartItem item) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
    }

    /**
     * Update the Shopping-cart item in Db
     *
     * @param item - the Shopping-cart item
     */
    public void update(ShoppingCartItem item) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        }
    }
}
