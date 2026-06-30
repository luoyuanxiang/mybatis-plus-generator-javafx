package com.example.generator.ui.util;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;

public final class JdbcUrlBuilder {

    private JdbcUrlBuilder() {
    }

    public static String buildUrl(DataSourceConfigDto config) {
        if (config.getJdbcUrl() != null && !config.getJdbcUrl().isBlank()) {
            return config.getJdbcUrl().trim();
        }
        DatabaseType type = config.getDbType();
        return switch (type) {
            case MYSQL -> String.format(
                    "jdbc:mysql://%s:%d/%s?%s",
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase(),
                    defaultParams(config.getJdbcParams(), "useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
            );
            case POSTGRESQL -> String.format(
                    "jdbc:postgresql://%s:%d/%s",
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase()
            );
            case SQLSERVER -> String.format(
                    "jdbc:sqlserver://%s:%d;databaseName=%s;encrypt=false",
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase()
            );
            case ORACLE -> String.format(
                    "jdbc:oracle:thin:@%s:%d:%s",
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase()
            );
            case H2 -> config.getDatabase().startsWith("jdbc:")
                    ? config.getDatabase()
                    : String.format("jdbc:h2:%s", config.getDatabase());
        };
    }

    private static String defaultParams(String params, String fallback) {
        return params == null || params.isBlank() ? fallback : params.trim();
    }
}
