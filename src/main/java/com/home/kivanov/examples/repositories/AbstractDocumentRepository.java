package com.home.kivanov.examples.repositories;

import com.home.kivanov.examples.documents.DocumentWithGoods;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;

import java.sql.*;
import java.util.*;

public abstract class AbstractDocumentRepository extends AbstractRepository implements Repository {

    @Override
    public Optional<DocumentWithGoods> get(Long id) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        prepareGetOneSQLQueryText()
                )
        ) {
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();

            final DocumentWithGoods result = getDocumentClassInstance();
            final List<StorageItem> storageItems = new ArrayList<>();

            while (resultSet.next()) {

                result.setId(resultSet.getLong(1));
                result.setNumber(resultSet.getString(2));
                result.setDateTime(
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

            result.setGoods(storageItems);

            if (result.getId() == null)
                return Optional.empty();

            return Optional.of(result);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public List<DocumentWithGoods> getAll() {
        try (
                final Connection connection = createConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(
                        prepareGetAllSQLQueryText()
                )
        ) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            final Map<Long, DocumentWithGoods> result = new HashMap<>();

            while (resultSet.next()) {

                final long currentId = resultSet.getLong(1);
                DocumentWithGoods currentDocument = result.get(currentId);

                if (currentDocument == null) {
                    currentDocument = createDocumentsWithInitialParameters(resultSet, currentId);
                    result.put(currentId, currentDocument);
                }

                currentDocument.getGoods().add(
                        new StorageItem(
                                resultSet.getLong(4),
                                new Goods(resultSet.getString(5)),
                                resultSet.getLong(6)
                        )
                );
            }

            if (result.isEmpty())
                return Collections.emptyList();

            return new ArrayList<>(result.values());

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<DocumentWithGoods> update(DocumentWithGoods document) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement updateDocumentStatement = connection.prepareStatement(
                        prepareUpdateDocumentHeadSQLQueryText()
                );

                final PreparedStatement deleteDocumentItemsStatement = connection.prepareStatement(
                        prepareDeleteDocumentItemsSQLQueryText()
                );

                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        prepareAddDocumentItemsSQLQueryText()
                )
        ) {
            updateDocumentStatement.setString(1, document.getNumber());
            updateDocumentStatement.setTimestamp(2, Timestamp.valueOf(document.getDateTime()));
            updateDocumentStatement.setLong(3, document.getId());
            updateDocumentStatement.executeUpdate();

            deleteDocumentItemsStatement.setLong(3, document.getId());
            deleteDocumentItemsStatement.executeUpdate();

            for (StorageItem storageItem : document.getGoods()) {
                createNewDocumentItemsStatement.setLong(1, document.getId());
                createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                createNewDocumentItemsStatement.executeUpdate();
            }

            return Optional.of(document);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    @Override
    public Optional<DocumentWithGoods> save(DocumentWithGoods document) {
        try (
                final Connection connection = createConnection();
                final PreparedStatement createDocumentStatement = connection.prepareStatement(
                        prepareCreateDocumentHeadSQLQueryText(),
                        Statement.RETURN_GENERATED_KEYS
                );
                final PreparedStatement createNewDocumentItemsStatement = connection.prepareStatement(
                        prepareAddDocumentItemsSQLQueryText()
                )
        ) {
            createDocumentStatement.setString(1, document.getNumber());
            createDocumentStatement.setTimestamp(2, Timestamp.valueOf(document.getDateTime()));
            createDocumentStatement.executeUpdate();

            final ResultSet generatedKeys = createDocumentStatement.getGeneratedKeys();

            if (generatedKeys.next()) {

                document.setId(generatedKeys.getLong(1));

                for (StorageItem storageItem : document.getGoods()) {
                    createNewDocumentItemsStatement.setLong(1, document.getId());
                    createNewDocumentItemsStatement.setLong(2, storageItem.getId());
                    createNewDocumentItemsStatement.executeUpdate();
                }
            }

            return Optional.of(document);

        } catch (Exception ignored) {
            //TODO: log it
        }

        return Optional.empty();
    }

    private DocumentWithGoods createDocumentsWithInitialParameters(ResultSet resultSet, long currentId) throws SQLException {
        final DocumentWithGoods result = getDocumentClassInstance();
        result.setId(currentId);
        result.setNumber(resultSet.getString(2));
        result.setDateTime(
                resultSet.getTimestamp(3).toLocalDateTime()
        );
        return result;
    }

    protected abstract String prepareGetAllSQLQueryText();

    protected abstract String prepareAddDocumentItemsSQLQueryText();

    protected abstract String prepareDeleteDocumentItemsSQLQueryText();

    protected abstract String prepareUpdateDocumentHeadSQLQueryText();

    protected abstract String prepareCreateDocumentHeadSQLQueryText();

    protected abstract String prepareGetOneSQLQueryText();

    protected abstract DocumentWithGoods getDocumentClassInstance();
}
