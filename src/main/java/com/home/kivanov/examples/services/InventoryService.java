package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.goods.StorageItem;

import java.util.List;

public interface InventoryService {

    Inventory findById(Long id);

    Inventory create();

    List<StorageItem> calculate();

    List<StorageItem> calculate(Inventory inventory);
}
