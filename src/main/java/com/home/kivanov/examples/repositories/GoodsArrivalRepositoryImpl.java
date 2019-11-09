package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;

import java.sql.*;
import java.util.*;

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
                                "ON goods_arrivals.id = arrivals_items.goods_arrival_id"
                )
        ) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            final Map<Long, GoodsArrival> goodsArrivalMap = new HashMap<>();

            while (resultSet.next()) {

                final long currentId = resultSet.getLong(1);
                GoodsArrival goodsArrival = goodsArrivalMap.get(currentId);

                if (goodsArrival == null) {
                    goodsArrival = createGoodsArrivalWithInitialParameters(resultSet, currentId);
                    goodsArrivalMap.put(currentId, goodsArrival);
                }

                goodsArrival.getGoods().add(
                        new StorageItem(
                                resultSet.getLong(4),
                                new Goods(resultSet.getString(5)),
                                resultSet.getLong(6)
                        )
                );
            }

            if (goodsArrivalMap.isEmpty())
                return Collections.emptyList();

            return new ArrayList<>(goodsArrivalMap.values());

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Collections.emptyList();
    }

    private GoodsArrival createGoodsArrivalWithInitialParameters(ResultSet resultSet, long currentId) throws SQLException {
        GoodsArrival goodsArrival = new GoodsArrival();
        goodsArrival.setId(currentId);
        goodsArrival.setNumber(resultSet.getString(2));
        goodsArrival.setDateTime(
                resultSet.getTimestamp(3).toLocalDateTime()
        );
        return goodsArrival;
    }

    @Override
    public Optional<GoodsArrival> update(GoodsArrival goodsArrival) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement updateDocumentStatement = connection.prepareStatement(
                        "UPDATE goods_arrivals SET number=?, date_time=? WHERE id = ?"
                );

                final PreparedStatement deleteDocumentItemsStatement = connection.prepareStatement(
                        "DELETE FROM goods_arrivals_storage_items WHERE goods_arrival_id=?"
                );

                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        "INSERT INTO goods_arrivals_storage_items (goods_arrival_id, storage_item_id) VALUES (?,?)"
                )
        ) {
            updateDocumentStatement.setString(1, goodsArrival.getNumber());
            updateDocumentStatement.setTimestamp(2, Timestamp.valueOf(goodsArrival.getDateTime()));
            updateDocumentStatement.setLong(3, goodsArrival.getId());
            updateDocumentStatement.executeUpdate();

            deleteDocumentItemsStatement.setLong(3, goodsArrival.getId());
            deleteDocumentItemsStatement.executeUpdate();

            for (StorageItem storageItem : goodsArrival.getGoods()) {
                createNewDocumentItemsStatement.setLong(1, goodsArrival.getId());
                createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                createNewDocumentItemsStatement.executeUpdate();
            }

            return Optional.of(goodsArrival);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public Optional<GoodsArrival> save(GoodsArrival goodsArrival) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement createDocumentStatement = connection.prepareStatement(
                        "INSERT INTO goods_arrivals (number,date_time) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        "INSERT INTO goods_arrivals_storage_items (goods_arrival_id, storage_item_id) VALUES (?,?)"
                )
        ) {
            createDocumentStatement.setString(1, goodsArrival.getNumber());
            createDocumentStatement.setTimestamp(2, Timestamp.valueOf(goodsArrival.getDateTime()));
            createDocumentStatement.executeUpdate();

            final ResultSet generatedKeys = createDocumentStatement.getGeneratedKeys();

            if (generatedKeys.next()) {

                goodsArrival.setId(generatedKeys.getLong(1));

                for (StorageItem storageItem : goodsArrival.getGoods()) {
                    createNewDocumentItemsStatement.setLong(1, goodsArrival.getId());
                    createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                    createNewDocumentItemsStatement.executeUpdate();
                }
            }

            return Optional.of(goodsArrival);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
