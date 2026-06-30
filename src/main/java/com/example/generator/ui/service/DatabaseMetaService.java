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

/**
 * 数据库元数据读取服务。
 * <p>
 * 基于 JDBC {@link DatabaseMetaData} 获取表列表与列详情，
 * 供 UI 表选择器与字段预览使用。不同数据库的 catalog/schema 解析逻辑各异。
 * </p>
 */
public class DatabaseMetaService {

    /**
     * 测试数据源连通性，委托 {@link JdbcConnectionUtil#testConnection}。
     */
    public void testConnection(DataSourceConfigDto config) throws Exception {
        JdbcConnectionUtil.testConnection(config);
    }

    /**
     * 列出数据源中所有表和视图。
     *
     * @param config 数据源配置
     * @return 按表名排序的 {@link TableMeta} 列表
     */
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
            tables.sort((a, b) -> a.name().compareToIgnoreCase(b.name()));
            return tables;
        }
    }

    /**
     * 列出指定表的所有列信息，含主键标记。
     *
     * @param config    数据源配置
     * @param tableName 表名
     * @return 列元数据列表，顺序与 JDBC 驱动返回一致
     */
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

    /** 读取表的主键列名集合。 */
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

    /**
     * 解析 JDBC catalog。
     * MySQL 使用 database 名作为 catalog，其他数据库使用连接的 catalog。
     */
    private String resolveCatalog(Connection connection, DataSourceConfigDto config) throws Exception {
        if (config.getDbType() == DatabaseType.MYSQL) {
            return config.getDatabase();
        }
        return connection.getCatalog();
    }

    /**
     * 解析 JDBC schema。
     * 优先使用配置中的 schema 字段，否则按数据库类型返回默认值。
     */
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
