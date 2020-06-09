package ru.nsu.distributed.dao;

import ru.nsu.distributed.schema.Tag;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class TagDAO extends DAO<Tag> {
    public TagDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insertSQL(Collection<Tag> entities) {
        if (entities.isEmpty()) return;
        try {
            var s = getConnection().createStatement();
            for (var n : entities) {
                var sql = "INSERT INTO public.tags(key, value) VALUES(" +
                        "'" + n.getK() + "'," +
                        "'" + n.getV() + "'" +
                        ")";
                s.execute(sql);
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertPreparedStatement(Collection<Tag> entities) {
        try {
            var ps = getConnection().prepareStatement("INSERT INTO public.tags(key, value) VALUES(?, ?)");
            for (var n : entities) {
                ps.setString(1, n.getK());
                ps.setString(2, n.getV());
                ps.executeUpdate();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertBatch(Collection<Tag> entities) {
        try {
            var ps = getConnection().prepareStatement("INSERT INTO public.tags(key, value) VALUES(?, ?)");
            for (var n : entities) {
                ps.setString(1, n.getK());
                ps.setString(2, n.getV());
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        try {
            var s = getConnection().createStatement();
            s.execute("DELETE FROM public.tags");
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
