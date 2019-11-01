package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

public class GoodsShipment extends AbstractStorageDocument {

    public GoodsShipment(StorageServiceImpl storage, String number, LocalDateTime dateTime, List<StorageItem> storageItems) {
        super(storage, number, dateTime, storageItems);
    }

    public void takeFromStorage() {
        goods.forEach(storageItem -> storage.take(storageItem));
    }
}
