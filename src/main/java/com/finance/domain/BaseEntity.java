package com.finance.domain;

public abstract class BaseEntity<T> {
    protected T id;
    public T getId() { return id; }
}
