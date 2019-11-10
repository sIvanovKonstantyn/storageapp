package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.DocumentWithGoods;
import com.home.kivanov.examples.documents.Inventory;

public class InventoryDocumentRepositoryImpl extends AbstractDocumentRepository {

    @Override
    public String prepareGetAllSQLQueryText() {
        return "SELECT inventories.id, " +
                "inventories.number, " +
                "inventories.date_time, " +
                "items.id AS itemID, " +
                "items.goods, " +
                "items.count " +
                "FROM inventories AS inventories " +
                "INNER JOIN inventories_storage_items inventories_items " +
                "INNER JOIN  storage_items items " +
                "ON items.id = inventories_items.storage_item_id " +
                "ON inventories.id = inventories_items.inventory_id";
    }

    @Override
    public String prepareAddDocumentItemsSQLQueryText() {
        return "INSERT INTO inventories_storage_items (inventory_id, storage_item_id) VALUES (?,?)";
    }

    @Override
    public String prepareDeleteDocumentItemsSQLQueryText() {
        return "DELETE FROM inventories_storage_items WHERE inventory_id=?";
    }

    @Override
    public String prepareUpdateDocumentHeadSQLQueryText() {
        return "UPDATE inventories SET number=?, date_time=? WHERE id = ?";
    }

    @Override
    public String prepareCreateDocumentHeadSQLQueryText() {
        return "INSERT INTO inventories (number,date_time) VALUES (?, ?)";
    }

    @Override
    public String prepareGetOneSQLQueryText() {
        return "SELECT inventories.id, " +
                "inventories.number, " +
                "inventories.date_time, " +
                "items.id AS itemID, " +
                "items.goods, " +
                "items.count " +
                "FROM inventories AS inventories " +
                "INNER JOIN inventories_storage_items inventories_items " +
                "INNER JOIN  storage_items items " +
                "ON items.id = inventories_items.storage_item_id " +
                "ON inventories.id = ? AND inventories.id = inventories_items.inventory_id";
    }

    @Override
    public DocumentWithGoods getDocumentClassInstance() {
        return new Inventory();
    }
}
