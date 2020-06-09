package ru.nsu.distributed.dao;

import ru.nsu.distributed.schema.Node;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class NodeDAO extends DAO<Node> {
    public NodeDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insertSQL(Collection<Node> entities) {
        if (entities.isEmpty()) return;
        try {
            var s = getConnection().createStatement();
            for (var n : entities) {
                var sql = "INSERT INTO public.nodes VALUES(" +
                        n.getId() + ", " +
                        n.getLon() + ", " +
                        n.getLat() + ", " +
                        n.getUid() + ", " +
                        "'" + n.getUser() + "'" +
                        ")";
                s.execute(sql);
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertPreparedStatement(Collection<Node> entities) {
        try {
            var ps = getConnection().prepareStatement("INSERT INTO public.nodes VALUES(?, ?, ?, ?, ?)");
            for (var n : entities) {
                ps.setBigDecimal(1, new BigDecimal(n.getId()));
                ps.setDouble(2, n.getLon());
                ps.setDouble(3, n.getLat());
                ps.setBigDecimal(4, new BigDecimal(n.getUid()));
                ps.setString(5, n.getUser());
                ps.executeUpdate();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertBatch(Collection<Node> entities) {
        try {
            var ps = getConnection().prepareStatement("INSERT INTO public.nodes VALUES(?, ?, ?, ?, ?)");
            for (var n : entities) {
                ps.setBigDecimal(1, new BigDecimal(n.getId()));
                ps.setDouble(2, n.getLon());
                ps.setDouble(3, n.getLat());
                ps.setBigDecimal(4, new BigDecimal(n.getUid()));
                ps.setString(5, n.getUser());
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
            s.execute("DELETE FROM public.nodes");
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
