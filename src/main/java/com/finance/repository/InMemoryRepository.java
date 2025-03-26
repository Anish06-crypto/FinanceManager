package com.finance.repository;
import com.finance.domain.BaseEntity;
import java.util.*;

public class InMemoryRepository<T extends BaseEntity<?>> implements Repository<T> {
    private final Map<Object, T> store = new HashMap<>();

    @Override
    public T save(T entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public T findById(Object id) {
        return Optional.ofNullable(store.get(id))
                .orElseThrow(() -> new NoSuchElementException("Entity not found: " + id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public T update(T entity) {
        if (!store.containsKey(entity.getId()))
            throw new NoSuchElementException("Entity not found: " + entity.getId());
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Object id) {
        if (store.remove(id) == null)
            throw new NoSuchElementException("Entity not found: " + id);
    }
}