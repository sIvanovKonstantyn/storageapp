package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoodsArrivalRepositoryImpl implements GoodsArrivalRepository {

    private final static String URL = "jdbc:postgresql://localhost:5432/storageAppDB/?user=postgres&password=postgres";

    @Override
    public Optional<GoodsArrival> get(Long id) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT goods_arrivals.id, " +
                                "goods_arrivals.number, " +
                                "goods_arrivals.date_time, " +
                                "items.id AS itemID, " +
                                "items.goods, " +
                                "items.count " +
                                "FROM goods_arrivals AS goods_arrivals " +
                                "INNER JOIN goods_arrivals_storage_items arrivals_items " +
                                "INNER JOIN  storage_items items " +
                                "ON items.id = arrivals_items.storage_item_id " +
                                "ON goods_arrivals.id = ? AND goods_arrivals.id = arrivals_items.goods_arrival_id"
                )
        ) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();

            final GoodsArrival goodsArrival = new GoodsArrival();
            final List<StorageItem> storageItems = new ArrayList<>();

            while (resultSet.next()) {

                goodsArrival.setId(resultSet.getLong(1));
                goodsArrival.setNumber(resultSet.getString(2));
                goodsArrival.setDateTime(
                        resultSet.getTimestamp(3).toLocalDateTime()
                );

                storageItems.add(
                        new StorageItem(
                                resultSet.getLong(4),
                                new Goods(resultSet.getString(5)),
                                resultSet.getLong(6)
                        )
                );
            }

            goodsArrival.setGoods(storageItems);

            if (goodsArrival.getId() == null)
                return Optional.empty();

            return Optional.of(goodsArrival);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public List<GoodsArrival> getAll() {
        return null;
    }

    @Override
    public Optional<GoodsArrival> update(GoodsArrival storageItem) {
        return Optional.empty();
    }

    @Override
    public Optional<GoodsArrival> save(GoodsArrival storageItem) {
        return Optional.empty();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
