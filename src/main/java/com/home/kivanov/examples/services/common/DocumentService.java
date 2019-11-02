package com.home.kivanov.examples.services.common;

public interface DocumentService<T> extends IDService<T> {
    T create();
}
