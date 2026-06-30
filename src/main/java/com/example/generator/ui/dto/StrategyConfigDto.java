package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StrategyConfigDto {

    private List<String> tablePrefixes = new ArrayList<>();
    private List<String> tableSuffixes = new ArrayList<>();
    private List<String> fieldPrefixes = new ArrayList<>();
    private List<String> fieldSuffixes = new ArrayList<>();
    private List<String> excludeTables = new ArrayList<>();

    private boolean enableLombok = true;
    private boolean enableTableFieldAnnotation = true;
    private boolean enableChainModel;
    private boolean enableRemoveIsPrefix;
    private boolean enableActiveRecord;
    private boolean entityFileOverride;
    private String logicDeleteColumnName = "";
    private String versionColumnName = "";
    private String namingStrategy = "underline_to_camel";
    private String columnNamingStrategy = "";
    private String idType = "AUTO";

    private boolean controllerRestStyle = true;
    private boolean controllerHyphenStyle;
    private boolean controllerFileOverride;

    private boolean mapperAnnotation;
    private boolean mapperBaseResultMap;
    private boolean mapperBaseColumnList;
    private boolean mapperFileOverride;

    private boolean serviceFileOverride;

    private String entityJavaTemplate = "";
    private String serviceTemplate = "";
    private String serviceImplTemplate = "";
    private String mapperTemplate = "";
    private String mapperXmlTemplate = "";
    private String controllerTemplate = "";

    private boolean disableEntity;
    private boolean disableService;
    private boolean disableMapper;
    private boolean disableController;

    public List<String> getTablePrefixes() {
        return tablePrefixes;
    }

    public void setTablePrefixes(List<String> tablePrefixes) {
        this.tablePrefixes = tablePrefixes;
    }

    public List<String> getTableSuffixes() {
        return tableSuffixes;
    }

    public void setTableSuffixes(List<String> tableSuffixes) {
        this.tableSuffixes = tableSuffixes;
    }

    public List<String> getFieldPrefixes() {
        return fieldPrefixes;
    }

    public void setFieldPrefixes(List<String> fieldPrefixes) {
        this.fieldPrefixes = fieldPrefixes;
    }

    public List<String> getFieldSuffixes() {
        return fieldSuffixes;
    }

    public void setFieldSuffixes(List<String> fieldSuffixes) {
        this.fieldSuffixes = fieldSuffixes;
    }

    public List<String> getExcludeTables() {
        return excludeTables;
    }

    public void setExcludeTables(List<String> excludeTables) {
        this.excludeTables = excludeTables;
    }

    public boolean isEnableLombok() {
        return enableLombok;
    }

    public void setEnableLombok(boolean enableLombok) {
        this.enableLombok = enableLombok;
    }

    public boolean isEnableTableFieldAnnotation() {
        return enableTableFieldAnnotation;
    }

    public void setEnableTableFieldAnnotation(boolean enableTableFieldAnnotation) {
        this.enableTableFieldAnnotation = enableTableFieldAnnotation;
    }

    public boolean isEnableChainModel() {
        return enableChainModel;
    }

    public void setEnableChainModel(boolean enableChainModel) {
        this.enableChainModel = enableChainModel;
    }

    public boolean isEnableRemoveIsPrefix() {
        return enableRemoveIsPrefix;
    }

    public void setEnableRemoveIsPrefix(boolean enableRemoveIsPrefix) {
        this.enableRemoveIsPrefix = enableRemoveIsPrefix;
    }

    public boolean isEnableActiveRecord() {
        return enableActiveRecord;
    }

    public void setEnableActiveRecord(boolean enableActiveRecord) {
        this.enableActiveRecord = enableActiveRecord;
    }

    public boolean isEntityFileOverride() {
        return entityFileOverride;
    }

    public void setEntityFileOverride(boolean entityFileOverride) {
        this.entityFileOverride = entityFileOverride;
    }

    public String getLogicDeleteColumnName() {
        return logicDeleteColumnName;
    }

    public void setLogicDeleteColumnName(String logicDeleteColumnName) {
        this.logicDeleteColumnName = logicDeleteColumnName;
    }

    public String getVersionColumnName() {
        return versionColumnName;
    }

    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
    }

    public String getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(String namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public String getColumnNamingStrategy() {
        return columnNamingStrategy;
    }

    public void setColumnNamingStrategy(String columnNamingStrategy) {
        this.columnNamingStrategy = columnNamingStrategy;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public boolean isControllerRestStyle() {
        return controllerRestStyle;
    }

    public void setControllerRestStyle(boolean controllerRestStyle) {
        this.controllerRestStyle = controllerRestStyle;
    }

    public boolean isControllerHyphenStyle() {
        return controllerHyphenStyle;
    }

    public void setControllerHyphenStyle(boolean controllerHyphenStyle) {
        this.controllerHyphenStyle = controllerHyphenStyle;
    }

    public boolean isControllerFileOverride() {
        return controllerFileOverride;
    }

    public void setControllerFileOverride(boolean controllerFileOverride) {
        this.controllerFileOverride = controllerFileOverride;
    }

    public boolean isMapperAnnotation() {
        return mapperAnnotation;
    }

    public void setMapperAnnotation(boolean mapperAnnotation) {
        this.mapperAnnotation = mapperAnnotation;
    }

    public boolean isMapperBaseResultMap() {
        return mapperBaseResultMap;
    }

    public void setMapperBaseResultMap(boolean mapperBaseResultMap) {
        this.mapperBaseResultMap = mapperBaseResultMap;
    }

    public boolean isMapperBaseColumnList() {
        return mapperBaseColumnList;
    }

    public void setMapperBaseColumnList(boolean mapperBaseColumnList) {
        this.mapperBaseColumnList = mapperBaseColumnList;
    }

    public boolean isMapperFileOverride() {
        return mapperFileOverride;
    }

    public void setMapperFileOverride(boolean mapperFileOverride) {
        this.mapperFileOverride = mapperFileOverride;
    }

    public boolean isServiceFileOverride() {
        return serviceFileOverride;
    }

    public void setServiceFileOverride(boolean serviceFileOverride) {
        this.serviceFileOverride = serviceFileOverride;
    }

    public String getEntityJavaTemplate() {
        return entityJavaTemplate;
    }

    public void setEntityJavaTemplate(String entityJavaTemplate) {
        this.entityJavaTemplate = entityJavaTemplate;
    }

    public String getServiceTemplate() {
        return serviceTemplate;
    }

    public void setServiceTemplate(String serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }

    public String getServiceImplTemplate() {
        return serviceImplTemplate;
    }

    public void setServiceImplTemplate(String serviceImplTemplate) {
        this.serviceImplTemplate = serviceImplTemplate;
    }

    public String getMapperTemplate() {
        return mapperTemplate;
    }

    public void setMapperTemplate(String mapperTemplate) {
        this.mapperTemplate = mapperTemplate;
    }

    public String getMapperXmlTemplate() {
        return mapperXmlTemplate;
    }

    public void setMapperXmlTemplate(String mapperXmlTemplate) {
        this.mapperXmlTemplate = mapperXmlTemplate;
    }

    public String getControllerTemplate() {
        return controllerTemplate;
    }

    public void setControllerTemplate(String controllerTemplate) {
        this.controllerTemplate = controllerTemplate;
    }

    public boolean isDisableEntity() {
        return disableEntity;
    }

    public void setDisableEntity(boolean disableEntity) {
        this.disableEntity = disableEntity;
    }

    public boolean isDisableService() {
        return disableService;
    }

    public void setDisableService(boolean disableService) {
        this.disableService = disableService;
    }

    public boolean isDisableMapper() {
        return disableMapper;
    }

    public void setDisableMapper(boolean disableMapper) {
        this.disableMapper = disableMapper;
    }

    public boolean isDisableController() {
        return disableController;
    }

    public void setDisableController(boolean disableController) {
        this.disableController = disableController;
    }
}
