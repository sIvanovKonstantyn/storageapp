package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageService;
import com.home.kivanov.examples.services.StorageServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractStorageDocument {

    protected Long id;
    protected StorageService storage;
    protected String number;
    protected LocalDateTime dateTime;
    protected List<StorageItem> goods;

    public AbstractStorageDocument(Long id, StorageService storage, String number, LocalDateTime dateTime, List<StorageItem> goods) {
        this.id = id;
        this.storage = storage;
        this.number = number;
        this.dateTime = dateTime;
        this.goods = goods;
    }

    public List<StorageItem> getGoods() {
        return goods;
    }
}
