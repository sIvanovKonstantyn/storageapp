package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;

import java.sql.*;
import java.util.*;

public class StorageRepositoryImpl implements StorageRepository {

    private final static String URL = "jdbc:postgresql://localhost:5432/storageAppDB/?user=postgres&password=postgres";

    @Override
    public Optional<StorageItem> get(Long id) {

        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id, goods, count FROM storage_items WHERE id = ?"
                )
        ) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                return Optional.of(
                        new StorageItem(
                                resultSet.getLong(1),
                                new Goods(resultSet.getString(2)),
                                resultSet.getLong(3)
                        )
                );


        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public List<StorageItem> getAll() {

        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id, goods, count FROM storage_items"
                )
        ) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            final List<StorageItem> result = new ArrayList<>();

            while (resultSet.next())
                result.add(
                        new StorageItem(
                                resultSet.getLong(1),
                                new Goods(resultSet.getString(2)),
                                resultSet.getLong(3)
                        )
                );


            return result;

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<StorageItem> update(StorageItem storageItem) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE storage_items SET goods=?, count=? WHERE id =?"
                )
        ) {
            preparedStatement.setString(1, getSerializedGoods(storageItem));
            preparedStatement.setLong(2, storageItem.getCount());
            preparedStatement.setLong(3, storageItem.getId());
            preparedStatement.executeUpdate();

            return Optional.of(storageItem);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    private String getSerializedGoods(StorageItem storageItem) {
        return new StringJoiner(";")
                .add(storageItem.getGoods().getName())
                .add(storageItem.getGoods().getDescription())
                .toString();
    }

    @Override
    public Optional<StorageItem> save(StorageItem storageItem) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO storage_items (goods, count) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            preparedStatement.setString(1, getSerializedGoods(storageItem));
            preparedStatement.setLong(2, storageItem.getCount());

            preparedStatement.executeUpdate();

            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                storageItem.setId(generatedKeys.getLong(1));

                return Optional.of(storageItem);
            }
        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
