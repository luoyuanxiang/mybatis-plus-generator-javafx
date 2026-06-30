package com.example.generator.ui.service;

import com.example.generator.ui.dto.ColumnMeta;
import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;
import com.example.generator.ui.dto.TableMeta;
import com.example.generator.ui.util.JdbcConnectionUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DatabaseMetaService {

    public void testConnection(DataSourceConfigDto config) throws Exception {
        JdbcConnectionUtil.testConnection(config);
    }

    public List<TableMeta> listTables(DataSourceConfigDto config) throws Exception {
        try (Connection connection = JdbcConnectionUtil.openConnection(config)) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = resolveCatalog(connection, config);
            String schema = resolveSchema(config);
            String[] types = new String[]{"TABLE", "VIEW"};

            List<TableMeta> tables = new ArrayList<>();
            try (ResultSet rs = metaData.getTables(catalog, schema, "%", types)) {
                while (rs.next()) {
                    String name = rs.getString("TABLE_NAME");
                    String comment = rs.getString("REMARKS");
                    String type = rs.getString("TABLE_TYPE");
                    if (name != null) {
                        tables.add(new TableMeta(name, comment == null ? "" : comment, type));
                    }
                }
            }
            tables.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            return tables;
        }
    }

    public List<ColumnMeta> listColumns(DataSourceConfigDto config, String tableName) throws Exception {
        try (Connection connection = JdbcConnectionUtil.openConnection(config)) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = resolveCatalog(connection, config);
            String schema = resolveSchema(config);
            Set<String> primaryKeys = loadPrimaryKeys(metaData, catalog, schema, tableName);

            List<ColumnMeta> columns = new ArrayList<>();
            try (ResultSet rs = metaData.getColumns(catalog, schema, tableName, "%")) {
                while (rs.next()) {
                    String name = rs.getString("COLUMN_NAME");
                    String type = rs.getString("TYPE_NAME");
                    int size = rs.getInt("COLUMN_SIZE");
                    boolean nullable = "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"));
                    String comment = rs.getString("REMARKS");
                    String defaultValue = rs.getString("COLUMN_DEF");
                    columns.add(new ColumnMeta(
                            name,
                            type,
                            size,
                            nullable,
                            primaryKeys.contains(name),
                            comment == null ? "" : comment,
                            defaultValue == null ? "" : defaultValue
                    ));
                }
            }
            return columns;
        }
    }

    private Set<String> loadPrimaryKeys(DatabaseMetaData metaData, String catalog, String schema, String tableName)
            throws Exception {
        Set<String> keys = new HashSet<>();
        try (ResultSet rs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
            while (rs.next()) {
                keys.add(rs.getString("COLUMN_NAME"));
            }
        }
        return keys;
    }

    private String resolveCatalog(Connection connection, DataSourceConfigDto config) throws Exception {
        if (config.getDbType() == DatabaseType.MYSQL) {
            return config.getDatabase();
        }
        return connection.getCatalog();
    }

    private String resolveSchema(DataSourceConfigDto config) {
        if (config.getSchema() != null && !config.getSchema().isBlank()) {
            return config.getSchema();
        }
        return switch (config.getDbType()) {
            case POSTGRESQL -> "public";
            case SQLSERVER -> "dbo";
            case ORACLE -> config.getUsername() == null ? null : config.getUsername().toUpperCase(Locale.ROOT);
            default -> null;
        };
    }
}
