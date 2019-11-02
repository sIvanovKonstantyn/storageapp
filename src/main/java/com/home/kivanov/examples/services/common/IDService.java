package com.home.kivanov.examples.services.common;

public interface IDService<T> {
    T findById(Long id);
}
