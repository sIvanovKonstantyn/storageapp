package com.home.kivanov.examples.services;

import com.home.kivanov.examples.goods.StorageItem;

import java.util.List;

public interface StorageService {

    StorageItem findById(Long id);

    void add(StorageItem storageItem);

    void take(StorageItem storageItem);

    List<StorageItem> findAll();
}
