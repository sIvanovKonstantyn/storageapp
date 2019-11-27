package com.home.kivanov.examples.services;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.repositories.GoodsArrivalDocumentRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GoodsArrivalServiceImpl implements GoodsArrivalService {

    private final GoodsArrivalDocumentRepositoryImpl goodsArrivalRepository;
    private final StorageService storageService;

    @Autowired
    public GoodsArrivalServiceImpl(StorageService storageService, GoodsArrivalDocumentRepositoryImpl goodsArrivalRepository) {
        this.storageService = storageService;
        this.goodsArrivalRepository = goodsArrivalRepository;
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
                .forEach(storageService::add);
    }
}
