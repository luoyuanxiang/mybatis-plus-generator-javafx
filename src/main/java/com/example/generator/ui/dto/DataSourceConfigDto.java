package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源连接配置。
 * <p>
 * 对应 UI「数据源管理」面板中的表单字段，序列化后持久化至 {@code ~/.mp-generator-ui/datasources.json}。
 * 当 {@link #jdbcUrl} 为空时，由 {@link com.example.generator.ui.util.JdbcUrlBuilder} 根据
 * {@link #dbType}、主机、端口等字段自动拼装 JDBC URL。
 * </p>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSourceConfigDto {

    /** 唯一标识，新建时由 {@link com.example.generator.ui.util.IdUtil} 生成 UUID。 */
    private String id;

    /** 数据源显示名称，用于下拉列表与配置方案关联。 */
    private String name;

    /** 数据库类型，决定默认端口、驱动类名及 URL 模板。 */
    private DatabaseType dbType = DatabaseType.MYSQL;

    /** 数据库主机地址；H2 内存库可留空或使用完整 JDBC URL。 */
    private String host = "127.0.0.1";

    /** 数据库端口；未填写时使用 {@link DatabaseType#getDefaultPort()}。 */
    private int port = 3306;

    /** 数据库名（MySQL/PostgreSQL/SQL Server/Oracle）或 H2 连接串片段。 */
    private String database = "test";

    /** JDBC 登录用户名。 */
    private String username = "root";

    /** JDBC 登录密码；若 {@link #savePassword} 为 false，持久化时会被清空。 */
    private String password = "";

    /** Schema 名称，PostgreSQL/SQL Server/Oracle 等场景可选。 */
    private String schema;

    /**
     * 完整 JDBC URL。
     * 非空时优先于 host/port/database 拼装逻辑，适用于自定义连接串场景。
     */
    private String jdbcUrl;

    /** MySQL 附加连接参数，如字符集、时区等。 */
    private String jdbcParams = "useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";

    /** 是否在本地 JSON 文件中保存密码；默认 false，避免明文泄露。 */
    private boolean savePassword;

    /**
     * 深拷贝当前配置，用于保存到仓库时避免引用共享。
     *
     * @return 字段值与当前实例相同的新对象
     */
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
