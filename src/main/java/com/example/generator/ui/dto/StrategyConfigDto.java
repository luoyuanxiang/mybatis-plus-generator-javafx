package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis-Plus {@code StrategyConfig} 对应的 UI 配置项。
 * <p>
 * 涵盖 Entity、Controller、Mapper、Service 各层的生成策略，
 * 包括命名规则、Lombok、逻辑删除、乐观锁、文件覆盖及自定义模板路径。
 * </p>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StrategyConfigDto {

    // ---- 表/字段过滤 ----

    /** 表名前缀，生成 Entity 时去除。 */
    private List<String> tablePrefixes = new ArrayList<>();

    /** 表名后缀，生成 Entity 时去除。 */
    private List<String> tableSuffixes = new ArrayList<>();

    /** 字段名前缀，生成属性时去除。 */
    private List<String> fieldPrefixes = new ArrayList<>();

    /** 字段名后缀，生成属性时去除。 */
    private List<String> fieldSuffixes = new ArrayList<>();

    /** 排除不参与生成的表名列表。 */
    private List<String> excludeTables = new ArrayList<>();

    // ---- Entity 策略 ----

    /** 是否生成 Lombok 注解（{@code @Data} 等）。 */
    private boolean enableLombok = true;

    /** 是否生成 {@code @TableField} 注解。 */
    private boolean enableTableFieldAnnotation = true;

    /** 是否启用链式 setter（{@code @Accessors(chain = true)}）。 */
    private boolean enableChainModel;

    /** 是否移除布尔字段 {@code is} 前缀。 */
    private boolean enableRemoveIsPrefix;

    /** 是否启用 ActiveRecord 模式。 */
    private boolean enableActiveRecord;

    /** 是否覆盖已存在的 Entity 文件。 */
    private boolean entityFileOverride;

    /** 逻辑删除字段名，空表示不启用。 */
    private String logicDeleteColumnName = "";

    /** 乐观锁版本字段名，空表示不启用。 */
    private String versionColumnName = "";

    /** 表名到 Entity 的命名策略：{@code underline_to_camel} 或 {@code no_change}。 */
    private String namingStrategy = "underline_to_camel";

    /** 列名到字段的命名策略，空则沿用 {@link #namingStrategy}。 */
    private String columnNamingStrategy = "";

    /** 主键类型：{@code AUTO}、{@code INPUT}、{@code ASSIGN_ID} 等。 */
    private String idType = "AUTO";

    // ---- Controller 策略 ----

    /** 是否生成 {@code @RestController} 风格。 */
    private boolean controllerRestStyle = true;

    /** 是否启用 URL 驼峰转连字符。 */
    private boolean controllerHyphenStyle;

    /** 是否覆盖已存在的 Controller 文件。 */
    private boolean controllerFileOverride;

    // ---- Mapper 策略 ----

    /** 是否在 Mapper 接口上添加 {@code @Mapper} 注解。 */
    private boolean mapperAnnotation;

    /** 是否生成 {@code BaseResultMap}。 */
    private boolean mapperBaseResultMap;

    /** 是否生成 {@code BaseColumnList}。 */
    private boolean mapperBaseColumnList;

    /** 是否覆盖已存在的 Mapper/XML 文件。 */
    private boolean mapperFileOverride;

    // ---- Service 策略 ----

    /** 是否覆盖已存在的 Service/ServiceImpl 文件。 */
    private boolean serviceFileOverride;

    // ---- 自定义模板路径 ----

    /** Entity Java 模板路径，空则使用内置默认模板。 */
    private String entityJavaTemplate = "";

    /** Service 接口模板路径。 */
    private String serviceTemplate = "";

    /** ServiceImpl 模板路径。 */
    private String serviceImplTemplate = "";

    /** Mapper 接口模板路径。 */
    private String mapperTemplate = "";

    /** Mapper XML 模板路径。 */
    private String mapperXmlTemplate = "";

    /** Controller 模板路径。 */
    private String controllerTemplate = "";

    // ---- 禁用某类文件生成 ----

    /** 是否跳过 Entity 生成。 */
    private boolean disableEntity;

    /** 是否跳过 Service/ServiceImpl 生成。 */
    private boolean disableService;

    /** 是否跳过 Mapper/XML 生成。 */
    private boolean disableMapper;

    /** 是否跳过 Controller 生成。 */
    private boolean disableController;
}
