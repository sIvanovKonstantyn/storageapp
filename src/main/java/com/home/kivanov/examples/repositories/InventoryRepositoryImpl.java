package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;

import java.sql.*;
import java.util.*;

public class InventoryRepositoryImpl implements InventoryRepository {

    private final static String URL = "jdbc:postgresql://localhost:5432/storageAppDB/?user=postgres&password=postgres";

    @Override
    public Optional<Inventory> get(Long id) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT inventories.id, " +
                                "inventories.number, " +
                                "inventories.date_time, " +
                                "items.id AS itemID, " +
                                "items.goods, " +
                                "items.count " +
                                "FROM inventories AS inventories " +
                                "INNER JOIN inventories_storage_items inventories_items " +
                                "INNER JOIN  storage_items items " +
                                "ON items.id = inventories_items.storage_item_id " +
                                "ON inventories.id = ? AND inventories.id = inventories_items.inventory_id"
                )
        ) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();

            final Inventory inventory = new Inventory();
            final List<StorageItem> storageItems = new ArrayList<>();

            while (resultSet.next()) {

                inventory.setId(resultSet.getLong(1));
                inventory.setNumber(resultSet.getString(2));
                inventory.setDateTime(
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

            inventory.setGoods(storageItems);

            if (inventory.getId() == null)
                return Optional.empty();

            return Optional.of(inventory);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public List<Inventory> getAll() {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT inventories.id, " +
                                "inventories.number, " +
                                "inventories.date_time, " +
                                "items.id AS itemID, " +
                                "items.goods, " +
                                "items.count " +
                                "FROM inventories AS inventories " +
                                "INNER JOIN inventories_storage_items inventories_items " +
                                "INNER JOIN  storage_items items " +
                                "ON items.id = inventories_items.storage_item_id " +
                                "ON inventories.id = inventories_items.inventory_id"
                )
        ) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            final Map<Long, Inventory> inventoryMap = new HashMap<>();

            while (resultSet.next()) {

                final long currentId = resultSet.getLong(1);
                Inventory inventory = inventoryMap.get(currentId);

                if (inventory == null) {
                    inventory = createInventoryWithInitialParameters(resultSet, currentId);
                    inventoryMap.put(currentId, inventory);
                }

                inventory.getGoods().add(
                        new StorageItem(
                                resultSet.getLong(4),
                                new Goods(resultSet.getString(5)),
                                resultSet.getLong(6)
                        )
                );
            }

            if (inventoryMap.isEmpty())
                return Collections.emptyList();

            return new ArrayList<>(inventoryMap.values());

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Collections.emptyList();
    }

    private Inventory createInventoryWithInitialParameters(ResultSet resultSet, long currentId) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setId(currentId);
        inventory.setNumber(resultSet.getString(2));
        inventory.setDateTime(
                resultSet.getTimestamp(3).toLocalDateTime()
        );
        return inventory;
    }

    @Override
    public Optional<Inventory> update(Inventory inventory) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement updateDocumentStatement = connection.prepareStatement(
                        "UPDATE inventories SET number=?, date_time=? WHERE id = ?"
                );

                final PreparedStatement deleteDocumentItemsStatement = connection.prepareStatement(
                        "DELETE FROM inventories_storage_items WHERE inventory_id=?"
                );

                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        "INSERT INTO inventories_storage_items (inventory_id, storage_item_id) VALUES (?,?)"
                )
        ) {
            updateDocumentStatement.setString(1, inventory.getNumber());
            updateDocumentStatement.setTimestamp(2, Timestamp.valueOf(inventory.getDateTime()));
            updateDocumentStatement.setLong(3, inventory.getId());
            updateDocumentStatement.executeUpdate();

            deleteDocumentItemsStatement.setLong(3, inventory.getId());
            deleteDocumentItemsStatement.executeUpdate();

            for (StorageItem storageItem : inventory.getGoods()) {
                createNewDocumentItemsStatement.setLong(1, inventory.getId());
                createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                createNewDocumentItemsStatement.executeUpdate();
            }

            return Optional.of(inventory);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public Optional<Inventory> save(Inventory inventory) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement createDocumentStatement = connection.prepareStatement(
                        "INSERT INTO inventories (number,date_time) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        "INSERT INTO inventories_storage_items (inventory_id, storage_item_id) VALUES (?,?)"
                )
        ) {
            createDocumentStatement.setString(1, inventory.getNumber());
            createDocumentStatement.setTimestamp(2, Timestamp.valueOf(inventory.getDateTime()));
            createDocumentStatement.executeUpdate();

            final ResultSet generatedKeys = createDocumentStatement.getGeneratedKeys();

            if (generatedKeys.next()) {

                inventory.setId(generatedKeys.getLong(1));

                for (StorageItem storageItem : inventory.getGoods()) {
                    createNewDocumentItemsStatement.setLong(1, inventory.getId());
                    createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                    createNewDocumentItemsStatement.executeUpdate();
                }
            }

            return Optional.of(inventory);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
