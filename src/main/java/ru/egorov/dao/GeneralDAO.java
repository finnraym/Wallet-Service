package ru.egorov.dao;

import java.util.Collection;
import java.util.Optional;

/**
 * The interface General dao.
 *
 * @param <U> the type parameter
 * @param <T> the type parameter
 */
public interface GeneralDAO<U, T>{
    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<T> findById(U id);

    /**
     * Find all collection.
     *
     * @return the collection
     */
    Collection<T> findAll();

    /**
     * Save t.
     *
     * @param entity the entity
     * @return the t
     */
    T save(T entity);
}
