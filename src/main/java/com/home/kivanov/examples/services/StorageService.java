package com.home.kivanov.examples.services;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.common.IDService;

import java.util.List;

public interface StorageService extends IDService<StorageItem> {

    void add(StorageItem storageItem);

    void take(StorageItem storageItem);

    List<StorageItem> findAll();
}
