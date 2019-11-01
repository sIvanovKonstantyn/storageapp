package com.home.kivanov.examples.storage;

import com.home.kivanov.examples.goods.StorageItem;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    private List<StorageItem> goods = new ArrayList<>();

    public void add(StorageItem storageItem) {

        final int i = goods.indexOf(storageItem);

        if (i >= 0) {
            goods.get(i).addCount(storageItem.getCount());
        } else {
            goods.add(storageItem);
        }
    }

    public void take(StorageItem storageItem) {

        final int i = goods.indexOf(storageItem);

        if (i >= 0) {
            goods.get(i).takeCount(storageItem.getCount());
        }
    }

    public List<StorageItem> getBalance() {
        return goods;
    }
}
