package com.example.generator.ui.util;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;
import lombok.experimental.UtilityClass;

/**
 * JDBC 连接 URL 构建工具。
 * <p>
 * 当 {@link DataSourceConfigDto#getJdbcUrl()} 已填写时直接返回；
 * 否则根据 {@link DatabaseType} 使用各数据库的标准 URL 模板拼装。
 * </p>
 */
@UtilityClass
public class JdbcUrlBuilder {

    /**
     * 根据数据源配置构建最终 JDBC URL。
     *
     * @param config 数据源配置，不可为 null
     * @return 可用于 {@link java.sql.DriverManager#getConnection} 的 URL
     */
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
                    defaultParams(config.getJdbcParams(),
                            "useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
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

    /** 连接参数为空时使用默认值。 */
    private static String defaultParams(String params, String fallback) {
        return params == null || params.isBlank() ? fallback : params.trim();
    }
}
