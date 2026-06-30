package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InjectionConfigDto {

    private Map<String, String> customMap = new HashMap<>();
    private List<CustomFileDto> customFiles = new ArrayList<>();
    private String templateEngine = "FREEMARKER";

    public Map<String, String> getCustomMap() {
        return customMap;
    }

    public void setCustomMap(Map<String, String> customMap) {
        this.customMap = customMap;
    }

    public List<CustomFileDto> getCustomFiles() {
        return customFiles;
    }

    public void setCustomFiles(List<CustomFileDto> customFiles) {
        this.customFiles = customFiles;
    }

    public String getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(String templateEngine) {
        this.templateEngine = templateEngine;
    }
}
