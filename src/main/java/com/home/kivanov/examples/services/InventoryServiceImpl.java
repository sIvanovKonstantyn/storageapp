package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.AbstractStorageDocument;
import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.goods.StorageItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryServiceImpl implements InventoryService {

    private List<Inventory> inventories = new ArrayList<>();

    private StorageService storageService;

    public InventoryServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Inventory findById(Long id) {
        return inventories
                .stream()
                .filter(inventory -> id.equals(inventory.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public Inventory create() {
        Long maxId = inventories
                .stream()
                .map(AbstractStorageDocument::getId)
                .max(Long::compareTo)
                .orElse(0L);

        final Inventory inventory = new Inventory(++maxId, "IN" + maxId);

        inventories.add(inventory);

        return inventory;
    }

    @Override
    public List<StorageItem> calculate() {
        return storageService.findAll();
    }

    @Override
    public List<StorageItem> calculate(Inventory inventory) {

        final List<StorageItem> storageItems = storageService.findAll();

        return inventory.getGoods()
                .stream()
                .map(storageItem -> {

                    final StorageItem item = storageItems
                            .stream()
                            .filter(currentItem -> currentItem.equals(storageItem))
                            .findAny()
                            .orElse(null);

                    if (item == null) {
                        return new StorageItem(storageItem.getId(), storageItem.getGoods(), -storageItem.getCount());
                    } else {
                        return new StorageItem(storageItem.getId(), storageItem.getGoods(), item.getCount() - storageItem.getCount());
                    }

                })
                .collect(Collectors.toList());
    }
}
