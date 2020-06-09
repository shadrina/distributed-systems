package ru.nsu.distributed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseInitializer implements AutoCloseable {
    private Connection connection;

    public DatabaseInitializer() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/distributed",
                "postgres",
                "postgres"
        );
        connection.setAutoCommit(false);
        var statement = connection.createStatement();
        statement.execute("CREATE OR REPLACE FUNCTION create_nodetable ()\n" +
                "  RETURNS void AS\n" +
                "$func$\n" +
                "BEGIN\n" +
                "   IF EXISTS (SELECT FROM pg_catalog.pg_tables \n" +
                "              WHERE  schemaname = 'public'\n" +
                "              AND    tablename  = 'nodes') THEN\n" +
                "      RAISE NOTICE 'Table public.nodes already exists.';\n" +
                "   ELSE\n" +
                "CREATE TABLE nodes(" +
                "id bigint primary key," +
                "lon numeric," +
                "lat numeric," +
                "uid bigint," +
                "\"user_name\" varchar);\n" +
                "   END IF;\n" +
                "END\n" +
                "$func$ LANGUAGE plpgsql;\n" +
                "SELECT create_nodetable()");
        statement.execute("CREATE OR REPLACE FUNCTION create_tagtable ()\n" +
                "  RETURNS void AS\n" +
                "$func$\n" +
                "BEGIN\n" +
                "   IF EXISTS (SELECT FROM pg_catalog.pg_tables \n" +
                "              WHERE  schemaname = 'public'\n" +
                "              AND    tablename  = 'tags') THEN\n" +
                "      RAISE NOTICE 'Table public.tags already exists.';\n" +
                "   ELSE\n" +
                "CREATE TABLE tags(" +
                "id serial primary key," +
                "key varchar," +
                "value varchar," +
                "node_id bigint);\n" +
                "   END IF;\n" +
                "END\n" +
                "$func$ LANGUAGE plpgsql;" +
                "SELECT create_tagtable()");
        statement.close();
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
