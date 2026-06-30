package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalConfigDto {

    private String author = "generator";
    private String outputDir = System.getProperty("user.home") + "/mp-generator-output";
    private boolean enableSwagger;
    private boolean disableOpenDir = true;
    private boolean enableKotlin;
    private String dateType = "TIME_PACK";
    private String commentDate = "yyyy-MM-dd";

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public boolean isEnableSwagger() {
        return enableSwagger;
    }

    public void setEnableSwagger(boolean enableSwagger) {
        this.enableSwagger = enableSwagger;
    }

    public boolean isDisableOpenDir() {
        return disableOpenDir;
    }

    public void setDisableOpenDir(boolean disableOpenDir) {
        this.disableOpenDir = disableOpenDir;
    }

    public boolean isEnableKotlin() {
        return enableKotlin;
    }

    public void setEnableKotlin(boolean enableKotlin) {
        this.enableKotlin = enableKotlin;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
