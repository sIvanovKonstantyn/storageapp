package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractStorageDocument implements DocumentWithGoods {

    private Long id;
    protected StorageService storage;
    protected String number;
    protected LocalDateTime dateTime;
    protected List<StorageItem> goods = new ArrayList<>();

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

    public AbstractStorageDocument() {}

    @Override
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public void setGoods(List<StorageItem> goods) {
        this.goods = goods;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
