package com.example.generator.ui.dto;

import com.example.generator.ui.service.DatabaseMetaService;

import java.sql.DatabaseMetaData;

/**
 * 数据库表元数据，用于 UI 表列表展示。
 * <p>
 * 由 {@link DatabaseMetaService#listTables} 通过
 * JDBC {@link DatabaseMetaData#getTables} 读取。
 * </p>
 *
 * @param name    表名。
 * @param comment 表注释（{@code REMARKS}）。
 * @param type    表类型，如 {@code TABLE}、{@code VIEW}。
 */
public record TableMeta(String name, String comment, String type) {

}
