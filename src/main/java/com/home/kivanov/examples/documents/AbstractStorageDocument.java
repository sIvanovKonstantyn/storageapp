package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public abstract class AbstractStorageDocument {

    private Long id;
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

    public AbstractStorageDocument(Long id, String number) {
        this.id = id;
        this.number = number;
        this.dateTime = LocalDateTime.now();
    }

    public List<StorageItem> getGoods() {
        return goods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractStorageDocument that = (AbstractStorageDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }
}
