package com.home.kivanov.examples.services;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.repositories.StorageRepository;
import com.home.kivanov.examples.repositories.StorageRepositoryImpl;

import java.util.List;

public class StorageServiceImpl implements StorageService {

    private StorageRepository storageRepository = new StorageRepositoryImpl();

    @Override
    public StorageItem findById(Long id) {
        return storageRepository
                .get(id)
                .orElse(null);
    }

    @Override
    public void add(StorageItem storageItem) {

        if (storageItem.getId() == null) {
            storageRepository.save(storageItem);
            return;
        }

        final StorageItem foundStorageItem = storageRepository
                .get(storageItem.getId())
                .orElse(null);

        if (foundStorageItem == null) {
            storageRepository.save(storageItem);
            return;
        }

        foundStorageItem.setCount(
                foundStorageItem.getCount() + storageItem.getCount()
        );

        storageRepository.update(foundStorageItem);
    }

    @Override
    public void take(StorageItem storageItem) {

        if (storageItem.getId() == null) {
            throw new IllegalArgumentException("StorageItem ID should be present");
        }

        final StorageItem foundStorageItem = storageRepository
                .get(storageItem.getId())
                .orElse(null);

        if (foundStorageItem == null) {
            throw new IllegalStateException("StorageItem be present in DB");
        }

        if (foundStorageItem.getCount() < storageItem.getCount()) {
            throw new IllegalStateException("Is not enough goods in storage");
        }

        foundStorageItem.setCount(
                foundStorageItem.getCount() - storageItem.getCount()
        );

        storageRepository.update(foundStorageItem);
    }

    @Override
    public List<StorageItem> findAll() {
        return storageRepository.getAll();
    }
}
