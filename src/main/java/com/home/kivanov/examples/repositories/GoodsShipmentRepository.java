package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.GoodsShipment;

import java.util.List;
import java.util.Optional;

public interface GoodsShipmentRepository {

    Optional<GoodsShipment> get(Long id);

    List<GoodsShipment> getAll();

    Optional<GoodsShipment> update(GoodsShipment storageItem);

    Optional<GoodsShipment> save(GoodsShipment storageItem);
}
