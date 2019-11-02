package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsShipment;
import com.home.kivanov.examples.services.common.DocumentService;

public interface GoodsShipmentService extends DocumentService<GoodsShipment> {
    void takeGoodsFromStorageByGoodsShipment(GoodsShipment document);
}
