package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MyBatis-Plus {@code PackageConfig} 对应的 UI 配置项。
 * <p>
 * 控制 Entity、Mapper、Service、Controller 及 XML 的包名与路径。
 * </p>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageConfigDto {

    /** 父包名，如 {@code com.example}。 */
    private String parent = "com.example";

    /** 模块名，可选，会拼接到 parent 之后。 */
    private String moduleName = "";

    /** Entity 类所在包名（相对 parent）。 */
    private String entity = "entity";

    /** Service 接口包名。 */
    private String service = "service";

    /** ServiceImpl 实现类包名。 */
    private String serviceImpl = "service.impl";

    /** Mapper 接口包名。 */
    private String mapper = "mapper";

    /** Controller 包名。 */
    private String controller = "controller";

    /** Mapper XML 默认包名标识。 */
    private String xml = "mapper.xml";

    /**
     * Mapper XML 独立输出目录。
     * 非空时通过 {@code OutputFile.xml} 覆盖默认路径。
     */
    private String mapperXmlPath = "";
}
