package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.common.DocumentService;

import java.util.List;

public interface InventoryService extends DocumentService<Inventory> {

    List<StorageItem> calculate();

    List<StorageItem> calculate(Inventory inventory);
}
