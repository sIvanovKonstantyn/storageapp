package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsArrival;

public interface GoodsArrivalService {

    GoodsArrival findById(Long id);

    GoodsArrival create();

    void addGoodsToStorageByGoodsArrival(GoodsArrival document);
}
