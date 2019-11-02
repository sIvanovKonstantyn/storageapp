package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsArrival;

public interface GoodsArrivalService {

    GoodsArrival findById(Long id);

    GoodsArrival create();

    GoodsArrival addGoodsToStorageByGoodsArrival(GoodsArrival document);
}
