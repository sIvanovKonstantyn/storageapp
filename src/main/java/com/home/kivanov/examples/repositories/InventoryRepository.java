package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    Optional<Inventory> get(Long id);

    List<Inventory> getAll();

    Optional<Inventory> update(Inventory storageItem);

    Optional<Inventory> save(Inventory storageItem);
}
