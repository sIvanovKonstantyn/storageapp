package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageService;

import java.time.LocalDateTime;
import java.util.List;

public class GoodsArrival extends AbstractStorageDocument {
    public GoodsArrival(StorageService storage, String number, LocalDateTime dateTime, List<StorageItem> storageItems) {
        super(storage, number, dateTime, storageItems);
    }

    public void addToStorage() {
        goods.forEach(
                storageItem -> storage.add(storageItem)
        );
    }
}
