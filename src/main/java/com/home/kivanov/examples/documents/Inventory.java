package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.storage.Storage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory extends AbstractStorageDocument {
    public Inventory(Storage storage, String number, LocalDateTime dateTime, List<StorageItem> storageItems) {
        super(storage, number, dateTime, storageItems);
    }

    public List<StorageItem> calculate() {

        final List<StorageItem> storageItems = storage.getBalance();

        return goods
                .stream()
                .map(storageItem -> {

                    final StorageItem item = storageItems
                            .stream()
                            .filter(currentItem -> currentItem.equals(storageItem))
                            .findAny()
                            .orElse(null);

                    if (item == null) {
                        return new StorageItem(storageItem.getGoods(), -storageItem.getCount());
                    } else {
                        return new StorageItem(storageItem.getGoods(), item.getCount() - storageItem.getCount());
                    }

                })
                .collect(Collectors.toList());
    }
}
