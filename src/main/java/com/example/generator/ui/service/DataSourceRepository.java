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

/**
 * 数据源配置的本地 JSON 持久化仓库。
 * <p>
 * 启动时从 {@link AppPaths#dataSourcesFile()} 加载，
 * 增删改操作后立即写回磁盘。密码是否持久化由 {@link DataSourceConfigDto#isSavePassword()} 控制。
 * </p>
 */
public class DataSourceRepository {

    /** 内存中的数据源列表，与 JSON 文件保持同步。 */
    private final List<DataSourceConfigDto> dataSources = new ArrayList<>();

    /** 构造时自动加载本地 JSON 文件。 */
    public DataSourceRepository() {
        load();
    }

    /** @return 所有数据源的防御性副本列表 */
    public List<DataSourceConfigDto> findAll() {
        return new ArrayList<>(dataSources);
    }

    /**
     * 按 ID 查找数据源。
     *
     * @param id 数据源唯一标识
     * @return 匹配的配置，不存在则 empty
     */
    public Optional<DataSourceConfigDto> findById(String id) {
        return dataSources.stream().filter(ds -> ds.getId().equals(id)).findFirst();
    }

    /**
     * 保存或更新数据源。
     * <p>
     * 新记录自动生成 ID；未勾选保存密码时清空 password 字段后再持久化。
     * </p>
     *
     * @param config 待保存配置
     */
    public void save(DataSourceConfigDto config) {
        if (config.getId() == null || config.getId().isBlank()) {
            config.setId(IdUtil.newId());
        }
        if (!config.isSavePassword()) {
            config.setPassword("");
        }
        dataSources.removeIf(ds -> ds.getId().equals(config.getId()));
        dataSources.add(config.copy());
        persist();
    }

    /**
     * 按 ID 删除数据源。
     *
     * @param id 数据源唯一标识
     */
    public void delete(String id) {
        dataSources.removeIf(ds -> ds.getId().equals(id));
        persist();
    }

    /** 从 JSON 文件加载数据源列表。 */
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

    /** 将当前内存列表写入 JSON 文件。 */
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
