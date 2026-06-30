package com.example.generator.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratorProfile {

    private String id;
    private String name = "default";
    private String dataSourceId;
    private GlobalConfigDto globalConfig = new GlobalConfigDto();
    private PackageConfigDto packageConfig = new PackageConfigDto();
    private StrategyConfigDto strategyConfig = new StrategyConfigDto();
    private InjectionConfigDto injectionConfig = new InjectionConfigDto();
    private List<String> selectedTables = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public GlobalConfigDto getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GlobalConfigDto globalConfig) {
        this.globalConfig = globalConfig;
    }

    public PackageConfigDto getPackageConfig() {
        return packageConfig;
    }

    public void setPackageConfig(PackageConfigDto packageConfig) {
        this.packageConfig = packageConfig;
    }

    public StrategyConfigDto getStrategyConfig() {
        return strategyConfig;
    }

    public void setStrategyConfig(StrategyConfigDto strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

    public InjectionConfigDto getInjectionConfig() {
        return injectionConfig;
    }

    public void setInjectionConfig(InjectionConfigDto injectionConfig) {
        this.injectionConfig = injectionConfig;
    }

    public List<String> getSelectedTables() {
        return selectedTables;
    }

    public void setSelectedTables(List<String> selectedTables) {
        this.selectedTables = selectedTables;
    }
}
