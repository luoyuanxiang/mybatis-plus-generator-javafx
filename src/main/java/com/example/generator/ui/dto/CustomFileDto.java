package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFileDto {

    private String fileName = "entityDTO.java";
    private String templatePath = "/templates/entityDTO.java.ftl";
    private String packageName = "dto";

    public CustomFileDto() {
    }

    public CustomFileDto(String fileName, String templatePath, String packageName) {
        this.fileName = fileName;
        this.templatePath = templatePath;
        this.packageName = packageName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
