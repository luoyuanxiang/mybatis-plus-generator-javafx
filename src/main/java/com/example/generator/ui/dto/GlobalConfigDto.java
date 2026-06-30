package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MyBatis-Plus {@code GlobalConfig} 对应的 UI 配置项。
 * <p>
 * 映射至 {@link com.baomidou.mybatisplus.generator.config.GlobalConfig.Builder}。
 * </p>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalConfigDto {

    /** 生成文件中的 {@code @author} 注释作者名。 */
    private String author = "generator";

    /** 代码输出根目录，生成前会自动创建。 */
    private String outputDir = System.getProperty("user.home") + "/mp-generator-output";

    /** 是否启用 Swagger 注解（{@code @Api} 等）。 */
    private boolean enableSwagger;

    /** 生成完成后是否禁止自动打开输出目录。 */
    private boolean disableOpenDir = true;

    /** 是否生成 Kotlin 代码而非 Java。 */
    private boolean enableKotlin;

    /**
     * 日期类型策略，对应 {@code DateType} 枚举名：
     * {@code ONLY_DATE}、{@code TIME_PACK}、{@code SQL_PACK}。
     */
    private String dateType = "TIME_PACK";

    /** 文件注释中的日期格式，如 {@code yyyy-MM-dd}。 */
    private String commentDate = "yyyy-MM-dd";
}
