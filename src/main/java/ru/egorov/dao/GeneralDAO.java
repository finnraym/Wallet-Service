package ru.egorov.dao;

import java.util.Collection;
import java.util.Optional;

public interface GeneralDAO<U, T>{
    Optional<T> findById(U id);
    Collection<T> findAll();
    T save(T entity);
}
