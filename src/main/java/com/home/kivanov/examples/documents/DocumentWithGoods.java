package com.home.kivanov.examples.documents;

import com.home.kivanov.examples.goods.StorageItem;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface DocumentWithGoods {
    void setId(Long id);

    void setNumber(String number);

    void setDateTime(LocalDateTime dateTime);

    void setGoods(List<StorageItem> storageItems);

    Long getId();

    Collection<StorageItem> getGoods();

    String getNumber();

    LocalDateTime getDateTime();
}
