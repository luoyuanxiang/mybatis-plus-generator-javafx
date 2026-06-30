package com.example.generator.ui.dto;

import com.example.generator.ui.service.DatabaseMetaService;

import java.sql.DatabaseMetaData;

/**
 * 数据库列元数据，用于 UI 字段预览表格展示。
 * <p>
 * 由 {@link DatabaseMetaService#listColumns} 通过
 * JDBC {@link DatabaseMetaData#getColumns} 读取。
 * </p>
 *
 * @param name         列名。
 * @param type         JDBC 类型名，如 {@code VARCHAR}、{@code BIGINT}。
 * @param size         列长度/精度（{@code COLUMN_SIZE}）。
 * @param nullable     是否允许 NULL。
 * @param primaryKey   是否为主键列。
 * @param comment      列注释（{@code REMARKS}）。
 * @param defaultValue 默认值（{@code COLUMN_DEF}）。
 */
public record ColumnMeta(String name, String type, int size, boolean nullable, boolean primaryKey, String comment,
                         String defaultValue) {

}
