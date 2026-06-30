package com.example.generator.ui.dto;

public enum DatabaseType {
    MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", 3306),
    POSTGRESQL("PostgreSQL", "org.postgresql.Driver", 5432),
    SQLSERVER("SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433),
    ORACLE("Oracle", "oracle.jdbc.OracleDriver", 1521),
    H2("H2", "org.h2.Driver", 0);

    private final String label;
    private final String driverClassName;
    private final int defaultPort;

    DatabaseType(String label, String driverClassName, int defaultPort) {
        this.label = label;
        this.driverClassName = driverClassName;
        this.defaultPort = defaultPort;
    }

    public String getLabel() {
        return label;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    @Override
    public String toString() {
        return label;
    }
}
