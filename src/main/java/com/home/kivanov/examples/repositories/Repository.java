package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.DocumentWithGoods;

import java.util.List;
import java.util.Optional;

public interface Repository {

    Optional<DocumentWithGoods> get(Long id);

    List<DocumentWithGoods> getAll();

    Optional<DocumentWithGoods> update(DocumentWithGoods storageItem);

    Optional<DocumentWithGoods> save(DocumentWithGoods storageItem);
}
