package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.config.MainConfiguration;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Component
@Scope("prototype")
public class StorageRepositoryImpl implements StorageRepository {

    private Connection connection;

    public StorageRepositoryImpl() {
        this.connection = new MainConfiguration().postgresConnection();
    }

//    @Autowired
//    public StorageRepositoryImpl(Connection connection) {
//        this.connection = connection;
//    }

    @Override
    public Optional<StorageItem> get(Long id) {

        try (
                final Connection connection = getConnection();
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

    private Connection getConnection() {
        return connection;
    }

    @Override
    public List<StorageItem> getAll() {

        try (
                final Connection connection = getConnection();
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
                final Connection connection = getConnection();
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
                final Connection connection = getConnection();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
