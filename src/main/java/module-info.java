module com.example.generator.ui {
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

    opens com.example.generator.ui.controller to javafx.fxml;
    opens com.example.generator.ui.dto to com.fasterxml.jackson.databind;

    exports com.example.generator.ui.app;
}
