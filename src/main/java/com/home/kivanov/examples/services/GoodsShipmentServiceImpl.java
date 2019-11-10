package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsShipment;
import com.home.kivanov.examples.repositories.GoodsShipmentDocumentRepositoryImpl;
import com.home.kivanov.examples.repositories.Repository;

public class GoodsShipmentServiceImpl implements GoodsShipmentService {

    private Repository goodsShipmentRepository = new GoodsShipmentDocumentRepositoryImpl();
    private StorageService storageService;

    public GoodsShipmentServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public GoodsShipment findById(Long id) {
        return (GoodsShipment) goodsShipmentRepository
                .get(id)
                .orElse(null);
    }

    @Override
    public GoodsShipment create() {
        return (GoodsShipment) goodsShipmentRepository
                .save(new GoodsShipment())
                .orElse(null);
    }

    @Override
    public void takeGoodsFromStorageByGoodsShipment(GoodsShipment goodsShipment) {

        if (goodsShipment.getId() == null) {
            goodsShipmentRepository.save(goodsShipment);
            return;
        }

        GoodsShipment foundGoodsShipment = (GoodsShipment) goodsShipmentRepository
                .get(goodsShipment.getId())
                .orElse(null);

        if (foundGoodsShipment == null) {
            goodsShipmentRepository.save(goodsShipment);
            return;
        }

        goodsShipmentRepository.update(goodsShipment);

        goodsShipment
                .getGoods()
                .forEach(storageItem -> storageService.take(storageItem));
    }
}
