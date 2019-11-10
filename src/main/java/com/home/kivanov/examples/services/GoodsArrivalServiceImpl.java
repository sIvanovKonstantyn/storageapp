package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.repositories.Repository;
import com.home.kivanov.examples.repositories.GoodsArrivalDocumentRepositoryImpl;

public class GoodsArrivalServiceImpl implements GoodsArrivalService {

    private Repository goodsArrivalRepository = new GoodsArrivalDocumentRepositoryImpl();
    private StorageService storageService;

    public GoodsArrivalServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public GoodsArrival findById(Long id) {
        return (GoodsArrival) goodsArrivalRepository
                .get(id)
                .orElse(null);
    }

    @Override
    public GoodsArrival create() {
        return (GoodsArrival) goodsArrivalRepository
                .save(new GoodsArrival())
                .orElse(null);
    }

    @Override
    public void addGoodsToStorageByGoodsArrival(GoodsArrival goodsArrival) {

        if (goodsArrival.getId() == null) {
            goodsArrivalRepository.save(goodsArrival);
            return;
        }

        GoodsArrival foundGoodsArrival = (GoodsArrival) goodsArrivalRepository
                .get(goodsArrival.getId())
                .orElse(null);

        if (foundGoodsArrival == null) {
            goodsArrivalRepository.save(goodsArrival);
            return;
        }

        goodsArrivalRepository.update(goodsArrival);

        goodsArrival
                .getGoods()
                .forEach(storageItem -> storageService.add(storageItem));
    }
}
