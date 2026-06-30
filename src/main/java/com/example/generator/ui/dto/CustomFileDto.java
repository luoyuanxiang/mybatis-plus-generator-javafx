package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomFile 自定义生成文件配置。
 * <p>
 * 对应 MyBatis-Plus {@code InjectionConfig.Builder#customFile}，
 * 允许在标准 Entity/Mapper 之外额外渲染 Freemarker 模板。
 * </p>
 *
 * @see InjectionConfigDto#getCustomFiles()
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFileDto {

    /** 输出文件名，如 {@code entityDTO.java}。 */
    private String fileName = "entityDTO.java";

    /**  classpath 下的模板路径，如 {@code /templates/entityDTO.java.ftl}。 */
    private String templatePath = "/templates/entityDTO.java.ftl";

    /** 生成文件的目标包名（相对 PackageConfig.parent）。 */
    private String packageName = "dto";
}
