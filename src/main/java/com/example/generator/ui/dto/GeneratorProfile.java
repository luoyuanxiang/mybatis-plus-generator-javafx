package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成配置方案（Profile）。
 * <p>
 * 聚合全局、包、策略、注入等子配置，并记录当前选中的表列表。
 * 持久化至 {@code ~/.mp-generator-ui/profiles.json}，支持导入/导出 JSON 文件。
 * </p>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratorProfile {

    /** 方案唯一标识。 */
    private String id;

    /** 方案显示名称，默认 {@code default}。 */
    private String name = "default";

    /** 关联的数据源 {@link DataSourceConfigDto#getId()}。 */
    private String dataSourceId;

    /** 全局配置：作者、输出目录、Swagger、日期类型等。 */
    private GlobalConfigDto globalConfig = new GlobalConfigDto();

    /** 包名配置：parent、entity、mapper、controller 等包路径。 */
    private PackageConfigDto packageConfig = new PackageConfigDto();

    /** 策略配置：Lombok、命名策略、文件覆盖、模板路径等。 */
    private StrategyConfigDto strategyConfig = new StrategyConfigDto();

    /** 注入配置：模板引擎、customMap、CustomFile 自定义文件。 */
    private InjectionConfigDto injectionConfig = new InjectionConfigDto();

    /** 本次生成选中的数据库表名列表。 */
    private List<String> selectedTables = new ArrayList<>();
}
