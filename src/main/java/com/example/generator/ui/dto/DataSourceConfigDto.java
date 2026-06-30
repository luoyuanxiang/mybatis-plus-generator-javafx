package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSourceConfigDto {

    private String id;
    private String name;
    private DatabaseType dbType = DatabaseType.MYSQL;
    private String host = "127.0.0.1";
    private int port = 3306;
    private String database = "test";
    private String username = "root";
    private String password = "";
    private String schema;
    private String jdbcUrl;
    private String jdbcParams = "useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private boolean savePassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DatabaseType getDbType() {
        return dbType;
    }

    public void setDbType(DatabaseType dbType) {
        this.dbType = dbType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcParams() {
        return jdbcParams;
    }

    public void setJdbcParams(String jdbcParams) {
        this.jdbcParams = jdbcParams;
    }

    public boolean isSavePassword() {
        return savePassword;
    }

    public void setSavePassword(boolean savePassword) {
        this.savePassword = savePassword;
    }

    public DataSourceConfigDto copy() {
        DataSourceConfigDto copy = new DataSourceConfigDto();
        copy.id = id;
        copy.name = name;
        copy.dbType = dbType;
        copy.host = host;
        copy.port = port;
        copy.database = database;
        copy.username = username;
        copy.password = password;
        copy.schema = schema;
        copy.jdbcUrl = jdbcUrl;
        copy.jdbcParams = jdbcParams;
        copy.savePassword = savePassword;
        return copy;
    }
}
