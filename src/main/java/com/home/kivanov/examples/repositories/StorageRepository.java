package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.goods.StorageItem;

import java.util.List;
import java.util.Optional;

public interface StorageRepository {

    Optional<StorageItem> get(Long id);

    List<StorageItem> getAll();

    void update(StorageItem storageItem);

    void save(StorageItem storageItem);
}
