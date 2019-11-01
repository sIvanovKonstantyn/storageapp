package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.storage.Storage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GoodsShipment extends AbstractStorageDocument {

    public GoodsShipment(Storage storage, String number, LocalDateTime dateTime, List<StorageItem> storageItems) {
        super(storage, number, dateTime, storageItems);
    }

    public void takeFromStorage() {
        goods.forEach(storageItem -> storage.take(storageItem));
    }
}
