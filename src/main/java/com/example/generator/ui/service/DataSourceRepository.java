package com.example.generator.ui.service;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.util.AppPaths;
import com.example.generator.ui.util.IdUtil;
import com.example.generator.ui.util.StringListUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataSourceRepository {

    private final List<DataSourceConfigDto> dataSources = new ArrayList<>();

    public DataSourceRepository() {
        load();
    }

    public List<DataSourceConfigDto> findAll() {
        return new ArrayList<>(dataSources);
    }

    public Optional<DataSourceConfigDto> findById(String id) {
        return dataSources.stream().filter(ds -> ds.getId().equals(id)).findFirst();
    }

    public DataSourceConfigDto save(DataSourceConfigDto config) {
        if (config.getId() == null || config.getId().isBlank()) {
            config.setId(IdUtil.newId());
        }
        if (!config.isSavePassword()) {
            config.setPassword("");
        }
        dataSources.removeIf(ds -> ds.getId().equals(config.getId()));
        dataSources.add(config.copy());
        persist();
        return config;
    }

    public void delete(String id) {
        dataSources.removeIf(ds -> ds.getId().equals(id));
        persist();
    }

    private void load() {
        try {
            AppPaths.ensureAppHome();
            if (!Files.exists(AppPaths.dataSourcesFile())) {
                return;
            }
            List<DataSourceConfigDto> loaded = StringListUtil.mapper().readValue(
                    AppPaths.dataSourcesFile().toFile(),
                    new TypeReference<List<DataSourceConfigDto>>() {
                    }
            );
            dataSources.clear();
            dataSources.addAll(loaded);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load data sources.", e);
        }
    }

    private void persist() {
        try {
            AppPaths.ensureAppHome();
            StringListUtil.mapper().writerWithDefaultPrettyPrinter()
                    .writeValue(AppPaths.dataSourcesFile().toFile(), dataSources);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save data sources.", e);
        }
    }
}
