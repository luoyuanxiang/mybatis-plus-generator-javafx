package com.example.generator.ui.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 支持的数据库类型枚举。
 * <p>
 * 每种类型绑定 UI 显示标签、JDBC 驱动类名及默认端口号，
 * 供 {@link com.example.generator.ui.util.JdbcUrlBuilder} 与
 * {@link com.example.generator.ui.util.JdbcConnectionUtil} 使用。
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum DatabaseType {

    /** MySQL / MariaDB，默认端口 3306。 */
    MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", 3306),

    /** PostgreSQL，默认端口 5432。 */
    POSTGRESQL("PostgreSQL", "org.postgresql.Driver", 5432),

    /** Microsoft SQL Server，默认端口 1433。 */
    SQLSERVER("SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433),

    /** Oracle Database，默认端口 1521。 */
    ORACLE("Oracle", "oracle.jdbc.OracleDriver", 1521),

    /** H2 嵌入式/内存库，端口无意义（填 0）。 */
    H2("H2", "org.h2.Driver", 0);

    /** UI 下拉框显示文本。 */
    private final String label;

    /** JDBC 驱动全限定类名，连接前通过 {@code Class.forName} 加载。 */
    private final String driverClassName;

    /** 新建数据源时的默认端口。 */
    private final int defaultPort;

    @Override
    public String toString() {
        return label;
    }
}
