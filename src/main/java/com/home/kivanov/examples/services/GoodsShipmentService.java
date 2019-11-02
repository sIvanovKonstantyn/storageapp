package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsShipment;

public interface GoodsShipmentService {

    GoodsShipment findById(Long id);

    GoodsShipment create();

    void takeGoodsFromStorageByGoodsShipment(GoodsShipment document);
}
