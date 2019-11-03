package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageService;

import java.time.LocalDateTime;
import java.util.List;

public class GoodsArrival extends AbstractStorageDocument {
    public GoodsArrival(Long id, StorageService storage, String number, LocalDateTime dateTime, List<StorageItem> storageItems) {
        super(id, storage, number, dateTime, storageItems);
    }

    public GoodsArrival(Long id, String number) {
        super(id, number);
    }

    public GoodsArrival() {
        super();
    }
}
