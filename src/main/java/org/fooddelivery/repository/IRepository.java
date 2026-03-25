package org.fooddelivery.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void save(T entity);
    void update(T entity);
    void delete(String id);
    Optional<T> findById(String id);
    List<T> findAll();
}