package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class StorageRepositoryImplTest {

    private StorageRepository storageRepository = new StorageRepositoryImpl();

    @Test
    void save() {
        final Optional<StorageItem> newStorageItem = storageRepository.save(
                new StorageItem(
                        new Goods("testGoodsName", "testGoodsDesc"),
                        10L
                )
        );
        assertNotNull(newStorageItem.orElse(null));
    }
}
