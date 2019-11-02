package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.AbstractStorageDocument;
import com.home.kivanov.examples.documents.GoodsArrival;

import java.util.ArrayList;
import java.util.List;

public class GoodsArrivalServiceImpl implements GoodsArrivalService {

    private List<GoodsArrival> goodsArrivals = new ArrayList<>();

    private StorageService storageService;

    public GoodsArrivalServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public GoodsArrival findById(Long id) {
        return goodsArrivals
                .stream()
                .filter(goodsArrival -> id.equals(goodsArrival.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public GoodsArrival create() {

        Long maxId = goodsArrivals
                .stream()
                .map(AbstractStorageDocument::getId)
                .max(Long::compareTo)
                .orElse(0L);

        final GoodsArrival goodsArrival = new GoodsArrival(++maxId, "GA" + maxId);

        goodsArrivals.add(goodsArrival);

        return goodsArrival;
    }

    @Override
    public void addGoodsToStorageByGoodsArrival(GoodsArrival document) {

        document
                .getGoods()
                .forEach(storageItem -> storageService.add(storageItem));
    }
}
