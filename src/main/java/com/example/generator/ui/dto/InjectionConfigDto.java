package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyBatis-Plus {@code InjectionConfig} 对应的 UI 配置项。
 * <p>
 * 用于向模板引擎注入自定义变量（customMap）及额外输出文件（CustomFile）。
 * </p>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InjectionConfigDto {

    /**
     * 注入到 Freemarker/Velocity 模板的自定义键值对。
     * 在模板中通过 {@code ${cfg.xxx}} 或 {@code ${custom.xxx}} 访问（取决于模板写法）。
     */
    private Map<String, String> customMap = new HashMap<>();

    /** 自定义输出文件列表，如 DTO、VO 等附加模板。 */
    private List<CustomFileDto> customFiles = new ArrayList<>();

    /**
     * 模板引擎类型：{@code FREEMARKER} 或 {@code VELOCITY}。
     * 默认 Freemarker。
     */
    private String templateEngine = "FREEMARKER";
}
