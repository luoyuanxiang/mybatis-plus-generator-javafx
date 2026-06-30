package com.example.generator.ui.util;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public final class JdbcConnectionUtil {

    private static final int CONNECT_TIMEOUT_SECONDS = 10;

    private JdbcConnectionUtil() {
    }

    public static Connection openConnection(DataSourceConfigDto config) throws Exception {
        DatabaseType type = config.getDbType();
        Class.forName(type.getDriverClassName());
        DriverManager.setLoginTimeout(CONNECT_TIMEOUT_SECONDS);
        String url = JdbcUrlBuilder.buildUrl(config);
        Properties props = new Properties();
        if (config.getUsername() != null) {
            props.setProperty("user", config.getUsername());
        }
        if (config.getPassword() != null) {
            props.setProperty("password", config.getPassword());
        }
        return DriverManager.getConnection(url, props);
    }

    public static void testConnection(DataSourceConfigDto config) throws Exception {
        try (Connection ignored = openConnection(config)) {
            if (!ignored.isValid((int) TimeUnit.SECONDS.toMillis(CONNECT_TIMEOUT_SECONDS))) {
                throw new IllegalStateException("Connection is not valid.");
            }
        }
    }
}
