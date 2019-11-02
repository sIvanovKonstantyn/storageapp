package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.AbstractStorageDocument;
import com.home.kivanov.examples.documents.GoodsShipment;

import java.util.ArrayList;
import java.util.List;

public class GoodsShipmentServiceImpl implements GoodsShipmentService {

    private List<GoodsShipment> goodsShipments = new ArrayList<>();

    private StorageService storageService;

    public GoodsShipmentServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public GoodsShipment findById(Long id) {
        return goodsShipments
                .stream()
                .filter(goodsShipment -> id.equals(goodsShipment.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public GoodsShipment create() {
        Long maxId = goodsShipments
                .stream()
                .map(AbstractStorageDocument::getId)
                .max(Long::compareTo)
                .orElse(0L);

        final GoodsShipment goodsShipment = new GoodsShipment(++maxId, "GS" + maxId);

        goodsShipments.add(goodsShipment);

        return goodsShipment;
    }

    @Override
    public void takeGoodsFromStorageByGoodsShipment(GoodsShipment document) {
        document.getGoods().forEach(storageItem -> storageService.take(storageItem));
    }
}
