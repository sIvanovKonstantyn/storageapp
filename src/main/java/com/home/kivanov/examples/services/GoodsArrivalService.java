package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.services.common.DocumentService;

public interface GoodsArrivalService extends DocumentService<GoodsArrival> {
    void addGoodsToStorageByGoodsArrival(GoodsArrival document);
}
