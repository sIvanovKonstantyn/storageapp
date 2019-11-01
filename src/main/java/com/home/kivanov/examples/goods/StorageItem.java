package com.home.kivanov.examples.goods;

import java.util.Objects;

public class StorageItem {

    private Goods goods;
    private long count;

    public StorageItem(Goods goods, long count) {
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
        return goods.equals(that.goods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goods);
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
}
