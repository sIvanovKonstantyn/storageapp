package com.home.kivanov.examples.goods;

import java.util.Objects;

public class StorageItem {

    private Long id;
    private Goods goods;
    private Long count;

    public StorageItem(Long id, Goods goods, Long count) {
        this.id = id;
        this.goods = goods;
        this.count = count;
    }

    public StorageItem(Goods goods, Long count) {
        this.goods = goods;
        this.count = count;
    }

    public Goods getGoods() {
        return goods;
    }

    public long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageItem that = (StorageItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addCount(long count) {
        this.count += count;
    }

    public void takeCount(long count) {
        this.count -= count;
    }

    @Override
    public String toString() {
        return "StorageItem{" +
                "goods=" + goods +
                ", count=" + count +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
