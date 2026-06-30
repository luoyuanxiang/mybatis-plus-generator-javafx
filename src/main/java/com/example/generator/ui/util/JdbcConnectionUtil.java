package com.example.generator.ui.util;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * JDBC 连接建立与连通性测试工具。
 */
@UtilityClass
public class JdbcConnectionUtil {

    /** 连接超时时间（秒），传递给 {@link DriverManager#setLoginTimeout}。 */
    private static final int CONNECT_TIMEOUT_SECONDS = 10;

    /**
     * 打开 JDBC 连接。
     * <p>
     * 调用方负责在 try-with-resources 中关闭连接。
     * </p>
     *
     * @param config 数据源配置
     * @return 已建立的 {@link Connection}
     * @throws Exception 驱动加载或连接失败时抛出
     */
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

    /**
     * 测试数据源是否可连通。
     *
     * @param config 数据源配置
     * @throws Exception 连接失败或 {@link Connection#isValid} 返回 false 时抛出
     */
    public static void testConnection(DataSourceConfigDto config) throws Exception {
        try (Connection ignored = openConnection(config)) {
            if (!ignored.isValid((int) TimeUnit.SECONDS.toMillis(CONNECT_TIMEOUT_SECONDS))) {
                throw new IllegalStateException("Connection is not valid.");
            }
        }
    }
}
