package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.GoodsShipment;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;

import java.sql.*;
import java.util.*;

public class GoodsShipmentRepositoryImpl implements GoodsShipmentRepository {

    private final static String URL = "jdbc:postgresql://localhost:5432/storageAppDB/?user=postgres&password=postgres";

    @Override
    public Optional<GoodsShipment> get(Long id) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT goods_shipments.id, " +
                                "goods_shipments.number, " +
                                "goods_shipments.date_time, " +
                                "items.id AS itemID, " +
                                "items.goods, " +
                                "items.count " +
                                "FROM goods_shipments AS goods_shipments " +
                                "INNER JOIN goods_shipments_storage_items shipments_items " +
                                "INNER JOIN  storage_items items " +
                                "ON items.id = shipments_items.storage_item_id " +
                                "ON goods_shipments.id = ? AND goods_shipments.id = shipments_items.goods_shipment_id"
                )
        ) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();

            final GoodsShipment goodsArrival = new GoodsShipment();
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
    public List<GoodsShipment> getAll() {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT goods_shipments.id, " +
                                "goods_shipments.number, " +
                                "goods_shipments.date_time, " +
                                "items.id AS itemID, " +
                                "items.goods, " +
                                "items.count " +
                                "FROM goods_shipments AS goods_shipments " +
                                "INNER JOIN goods_shipments_storage_items shipments_items " +
                                "INNER JOIN  storage_items items " +
                                "ON items.id = shipments_items.storage_item_id " +
                                "ON goods_shipments.id = shipments_items.goods_shipment_id"
                )
        ) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            final Map<Long, GoodsShipment> goodsShipmentMap = new HashMap<>();

            while (resultSet.next()) {

                final long currentId = resultSet.getLong(1);
                GoodsShipment goodsShipment = goodsShipmentMap.get(currentId);

                if (goodsShipment == null) {
                    goodsShipment = createGoodsShipmentWithInitialParameters(resultSet, currentId);
                    goodsShipmentMap.put(currentId, goodsShipment);
                }

                goodsShipment.getGoods().add(
                        new StorageItem(
                                resultSet.getLong(4),
                                new Goods(resultSet.getString(5)),
                                resultSet.getLong(6)
                        )
                );
            }

            if (goodsShipmentMap.isEmpty())
                return Collections.emptyList();

            return new ArrayList<>(goodsShipmentMap.values());

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Collections.emptyList();
    }

    private GoodsShipment createGoodsShipmentWithInitialParameters(ResultSet resultSet, long currentId) throws SQLException {
        GoodsShipment goodsShipment = new GoodsShipment();
        goodsShipment.setId(currentId);
        goodsShipment.setNumber(resultSet.getString(2));
        goodsShipment.setDateTime(
                resultSet.getTimestamp(3).toLocalDateTime()
        );
        return goodsShipment;
    }

    @Override
    public Optional<GoodsShipment> update(GoodsShipment goodsShipment) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement updateDocumentStatement = connection.prepareStatement(
                        "UPDATE goods_shipments SET number=?, date_time=? WHERE id = ?"
                );

                final PreparedStatement deleteDocumentItemsStatement = connection.prepareStatement(
                        "DELETE FROM goods_shipments_storage_items WHERE goods_shipment_id=?"
                );

                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        "INSERT INTO goods_shipments_storage_items (goods_shipment_id, storage_item_id) VALUES (?,?)"
                )
        ) {
            updateDocumentStatement.setString(1, goodsShipment.getNumber());
            updateDocumentStatement.setTimestamp(2, Timestamp.valueOf(goodsShipment.getDateTime()));
            updateDocumentStatement.setLong(3, goodsShipment.getId());
            updateDocumentStatement.executeUpdate();

            deleteDocumentItemsStatement.setLong(3, goodsShipment.getId());
            deleteDocumentItemsStatement.executeUpdate();

            for (StorageItem storageItem : goodsShipment.getGoods()) {
                createNewDocumentItemsStatement.setLong(1, goodsShipment.getId());
                createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                createNewDocumentItemsStatement.executeUpdate();
            }

            return Optional.of(goodsShipment);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public Optional<GoodsShipment> save(GoodsShipment goodsShipment) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement createDocumentStatement = connection.prepareStatement(
                        "INSERT INTO goods_shipments (number,date_time) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        "INSERT INTO goods_shipments_storage_items (goods_shipment_id, storage_item_id) VALUES (?,?)"
                )
        ) {
            createDocumentStatement.setString(1, goodsShipment.getNumber());
            createDocumentStatement.setTimestamp(2, Timestamp.valueOf(goodsShipment.getDateTime()));
            createDocumentStatement.executeUpdate();

            final ResultSet generatedKeys = createDocumentStatement.getGeneratedKeys();

            if (generatedKeys.next()) {

                goodsShipment.setId(generatedKeys.getLong(1));

                for (StorageItem storageItem : goodsShipment.getGoods()) {
                    createNewDocumentItemsStatement.setLong(1, goodsShipment.getId());
                    createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                    createNewDocumentItemsStatement.executeUpdate();
                }
            }

            return Optional.of(goodsShipment);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
