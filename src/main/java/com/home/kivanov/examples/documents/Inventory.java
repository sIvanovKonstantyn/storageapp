package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageService;

import java.time.LocalDateTime;
import java.util.List;

public class Inventory extends AbstractStorageDocument {
    public Inventory(Long id, Storage storage, String number, LocalDateTime dateTime, List<StorageItem> storageItems) {
        super(id, storage, number, dateTime, storageItems);
    }

    public Inventory(Long id, String number) {
        super(id, number);
    }

    public Inventory() {
        super();
    }
}
