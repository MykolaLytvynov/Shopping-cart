package com.mykola.shoppingcart.dao;

import java.util.Optional;

public interface CrudOperations<T, ID> {

    /**
     * Save an entity to Db
     *
     * @param entity
     * @return saved entity
     */
    T save(T entity);

    /**
     * Get entity from Db
     *
     * @param id - id entity
     * @return Optional entity
     */
    Optional<T> findById(ID id);

    /**
     * Delete entity from Db
     *
     * @param entity - entity
     */
    void delete(T entity);
}
