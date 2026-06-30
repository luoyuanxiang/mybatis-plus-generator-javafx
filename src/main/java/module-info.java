/**
 * JPMS 模块描述：MyBatis-Plus Generator JavaFX 桌面应用。
 * <p>
 * 声明 JavaFX、Jackson、MyBatis-Plus、JDBC 驱动等运行时依赖，
 * 并对 FXML/Jackson 反射所需的包执行 {@code opens}。
 * </p>
 */
module com.example.generator.ui {
    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires freemarker;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.generator;
    requires com.baomidou.mybatis.plus.core;

    requires mysql.connector.j;
    requires org.postgresql.jdbc;
    requires com.microsoft.sqlserver.jdbc;
    requires com.oracle.database.jdbc;
    requires com.h2database;
    requires org.mybatis;

    opens com.example.generator.ui.controller to javafx.fxml;
    opens com.example.generator.ui.dto to com.fasterxml.jackson.databind;

    exports com.example.generator.ui.app;
}
