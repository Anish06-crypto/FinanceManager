package com.finance.repository;
import com.finance.domain.BaseEntity;
import java.util.List;

public interface Repository<T extends BaseEntity<?>> {
    T save(T entity);
    T findById(Object id);
    List<T> findAll();
    T update(T entity);
    void delete(Object id);
}