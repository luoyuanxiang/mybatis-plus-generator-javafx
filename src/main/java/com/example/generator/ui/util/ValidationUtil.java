package com.example.generator.ui.util;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static void validateDataSource(DataSourceConfigDto config) {
        if (config == null) {
            throw new IllegalArgumentException("Data source config is required.");
        }
        if (config.getName() == null || config.getName().isBlank()) {
            throw new IllegalArgumentException("Data source name is required.");
        }
        if (config.getDbType() == null) {
            throw new IllegalArgumentException("Database type is required.");
        }
        if (config.getDbType() != DatabaseType.H2
                && (config.getJdbcUrl() == null || config.getJdbcUrl().isBlank())
                && (config.getHost() == null || config.getHost().isBlank())) {
            throw new IllegalArgumentException("Host or JDBC URL is required.");
        }
    }
}
