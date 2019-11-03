package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.GoodsArrival;

import java.util.List;
import java.util.Optional;

public interface GoodsArrivalRepository {

    Optional<GoodsArrival> get(Long id);

    List<GoodsArrival> getAll();

    Optional<GoodsArrival> update(GoodsArrival storageItem);

    Optional<GoodsArrival> save(GoodsArrival storageItem);
}
