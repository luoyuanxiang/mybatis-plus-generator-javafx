package com.example.generator.ui.util;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;
import lombok.experimental.UtilityClass;

/**
 * 数据源表单校验工具。
 */
@UtilityClass
public class ValidationUtil {

    /**
     * 校验数据源配置必填项。
     *
     * @param config 待校验配置
     * @throws IllegalArgumentException 校验不通过时抛出，消息可直接展示给用户
     */
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
        // H2 允许仅填写 database 片段（如 mem:test），其他类型需 host 或完整 jdbcUrl
        if (config.getDbType() != DatabaseType.H2
                && (config.getJdbcUrl() == null || config.getJdbcUrl().isBlank())
                && (config.getHost() == null || config.getHost().isBlank())) {
            throw new IllegalArgumentException("Host or JDBC URL is required.");
        }
    }
}
