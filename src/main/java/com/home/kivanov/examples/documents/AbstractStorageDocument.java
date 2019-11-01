package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractStorageDocument {

    protected StorageServiceImpl storage;
    protected String number;
    protected LocalDateTime dateTime;
    protected List<StorageItem> goods;

    public AbstractStorageDocument(StorageServiceImpl storage, String number, LocalDateTime dateTime, List<StorageItem> goods) {
        this.storage = storage;
        this.number = number;
        this.dateTime = dateTime;
        this.goods = goods;
    }

    public List<StorageItem> getGoods() {
        return goods;
    }
}
