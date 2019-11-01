package com.home.kivanov.examples.services;

import com.home.kivanov.examples.goods.StorageItem;

public interface StorageService {

    StorageItem findById(Long id);

    void add(StorageItem storageItem);

    void take(StorageItem storageItem);

    Iterable<StorageItem> findAll();
}
