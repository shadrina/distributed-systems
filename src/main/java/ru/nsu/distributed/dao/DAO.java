package ru.nsu.distributed.dao;

import java.sql.Connection;
import java.util.Collection;

abstract class DAO<T> {
    private Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() {
        return connection;
    }

    public abstract void insertSQL(Collection<T> entities);
    public abstract void insertPreparedStatement(Collection<T> entities);
    public abstract void insertBatch(Collection<T> entities);
    public abstract void deleteAll();
}
