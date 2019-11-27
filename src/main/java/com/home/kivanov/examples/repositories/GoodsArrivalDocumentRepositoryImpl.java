package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.GoodsArrival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Scope("prototype")
public class GoodsArrivalDocumentRepositoryImpl extends AbstractDocumentRepository {

    @Autowired
    public GoodsArrivalDocumentRepositoryImpl() {
        super();
    }

    @Override
    protected GoodsArrival getDocumentClassInstance() {
        return new GoodsArrival();
    }

    @Override
    protected String prepareGetOneSQLQueryText() {
        return "SELECT goods_arrivals.id, " +
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
                ;
    }

    @Override
    protected String prepareGetAllSQLQueryText() {
        return "SELECT goods_arrivals.id, " +
                "goods_arrivals.number, " +
                "goods_arrivals.date_time, " +
                "items.id AS itemID, " +
                "items.goods, " +
                "items.count " +
                "FROM goods_arrivals AS goods_arrivals " +
                "INNER JOIN goods_arrivals_storage_items arrivals_items " +
                "INNER JOIN  storage_items items " +
                "ON items.id = arrivals_items.storage_item_id " +
                "ON goods_arrivals.id = arrivals_items.goods_arrival_id";
    }

    @Override
    protected String prepareAddDocumentItemsSQLQueryText() {
        return "INSERT INTO goods_arrivals_storage_items (goods_arrival_id, storage_item_id) VALUES (?,?)";
    }

    @Override
    protected String prepareDeleteDocumentItemsSQLQueryText() {
        return "DELETE FROM goods_arrivals_storage_items WHERE goods_arrival_id=?";
    }

    @Override
    protected String prepareUpdateDocumentHeadSQLQueryText() {
        return "UPDATE goods_arrivals SET number=?, date_time=? WHERE id = ?";
    }

    @Override
    protected String prepareCreateDocumentHeadSQLQueryText() {
        return "INSERT INTO goods_arrivals (number,date_time) VALUES (?, ?)";
    }
}
