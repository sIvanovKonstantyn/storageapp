package com.home.kivanov.examples.services;

import com.home.kivanov.examples.goods.StorageItem;

import java.util.ArrayList;
import java.util.List;

public class StorageServiceImpl implements StorageService {

    private List<StorageItem> storageItems = new ArrayList<>();

    @Override
    public StorageItem findById(Long id) {
        return storageItems
                .stream()
                .filter(storageItem -> id.equals(storageItem.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public void add(StorageItem storageItem) {

        final int i = storageItems.indexOf(storageItem);

        if (i >= 0) {
            storageItems.get(i).addCount(storageItem.getCount());
        } else {
            storageItems.add(storageItem);
        }
    }

    @Override
    public void take(StorageItem storageItem) {

        final int i = storageItems.indexOf(storageItem);

        if (i >= 0) {
            storageItems.get(i).takeCount(storageItem.getCount());
        }
    }

    @Override
    public List<StorageItem> findAll() {
        return storageItems;
    }
}
