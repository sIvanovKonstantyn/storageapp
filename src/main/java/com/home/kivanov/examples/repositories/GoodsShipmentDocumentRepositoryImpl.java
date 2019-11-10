package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.DocumentWithGoods;
import com.home.kivanov.examples.documents.GoodsShipment;

public class GoodsShipmentDocumentRepositoryImpl extends AbstractDocumentRepository {

    @Override
    public String prepareGetAllSQLQueryText() {
        return "SELECT goods_shipments.id, " +
                "goods_shipments.number, " +
                "goods_shipments.date_time, " +
                "items.id AS itemID, " +
                "items.goods, " +
                "items.count " +
                "FROM goods_shipments AS goods_shipments " +
                "INNER JOIN goods_shipments_storage_items shipments_items " +
                "INNER JOIN  storage_items items " +
                "ON items.id = shipments_items.storage_item_id " +
                "ON goods_shipments.id = shipments_items.goods_shipment_id";
    }

    @Override
    public String prepareAddDocumentItemsSQLQueryText() {
        return "INSERT INTO goods_shipments_storage_items (goods_shipment_id, storage_item_id) VALUES (?,?)";
    }

    @Override
    public String prepareDeleteDocumentItemsSQLQueryText() {
        return "DELETE FROM goods_shipments_storage_items WHERE goods_shipment_id=?";
    }

    @Override
    public String prepareUpdateDocumentHeadSQLQueryText() {
        return "UPDATE goods_shipments SET number=?, date_time=? WHERE id = ?";
    }

    @Override
    public String prepareCreateDocumentHeadSQLQueryText() {
        return "INSERT INTO goods_shipments (number,date_time) VALUES (?, ?)";
    }

    @Override
    public String prepareGetOneSQLQueryText() {
        return "SELECT goods_shipments.id, " +
                "goods_shipments.number, " +
                "goods_shipments.date_time, " +
                "items.id AS itemID, " +
                "items.goods, " +
                "items.count " +
                "FROM goods_shipments AS goods_shipments " +
                "INNER JOIN goods_shipments_storage_items shipments_items " +
                "INNER JOIN  storage_items items " +
                "ON items.id = shipments_items.storage_item_id " +
                "ON goods_shipments.id = ? AND goods_shipments.id = shipments_items.goods_shipment_id";
    }

    @Override
    public DocumentWithGoods getDocumentClassInstance() {
        return new GoodsShipment();
    }
}
