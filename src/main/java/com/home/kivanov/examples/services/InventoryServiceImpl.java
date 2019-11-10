package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.repositories.InventoryDocumentRepositoryImpl;
import com.home.kivanov.examples.repositories.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryServiceImpl implements InventoryService {

    private Repository inventoryRepository = new InventoryDocumentRepositoryImpl();
    private StorageService storageService;

    public InventoryServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Inventory findById(Long id) {
        return (Inventory) inventoryRepository
                .get(id)
                .orElse(null);
    }

    @Override
    public Inventory create() {
        return (Inventory) inventoryRepository
                .save(new Inventory())
                .orElse(null);
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
