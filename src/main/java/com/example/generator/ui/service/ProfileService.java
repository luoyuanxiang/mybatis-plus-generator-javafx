package com.example.generator.ui.service;

import com.example.generator.ui.dto.GeneratorProfile;
import com.example.generator.ui.util.AppPaths;
import com.example.generator.ui.util.IdUtil;
import com.example.generator.ui.util.StringListUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 生成配置方案（Profile）的本地 JSON 持久化服务。
 * <p>
 * 支持方案的 CRUD、JSON 导入/导出。首次启动若文件不存在，会自动创建名为 {@code default} 的默认方案。
 * </p>
 */
public class ProfileService {

    /** 内存中的配置方案列表。 */
    private final List<GeneratorProfile> profiles = new ArrayList<>();

    /** 构造时加载本地 JSON，不存在则初始化默认方案。 */
    public ProfileService() {
        load();
    }

    /** @return 所有方案的防御性副本列表 */
    public List<GeneratorProfile> findAll() {
        return new ArrayList<>(profiles);
    }

    /**
     * 按 ID 查找配置方案。
     *
     * @param id 方案唯一标识
     * @return 匹配的方案，不存在则 empty
     */
    public Optional<GeneratorProfile> findById(String id) {
        return profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    /**
     * 保存或更新配置方案（深拷贝后写入内存与磁盘）。
     *
     * @param profile 待保存方案
     * @return 传入的方案对象（含 ID）
     */
    public GeneratorProfile save(GeneratorProfile profile) {
        if (profile.getId() == null || profile.getId().isBlank()) {
            profile.setId(IdUtil.newId());
        }
        profiles.removeIf(p -> p.getId().equals(profile.getId()));
        profiles.add(copyProfile(profile));
        persist();
        return profile;
    }

    /** 按 ID 删除配置方案。 */
    public void delete(String id) {
        profiles.removeIf(p -> p.getId().equals(id));
        persist();
    }

    /**
     * 从外部 JSON 文件导入方案并保存。
     *
     * @param file JSON 文件路径
     * @return 导入并保存后的方案
     */
    public GeneratorProfile importFromFile(Path file) throws Exception {
        GeneratorProfile profile = StringListUtil.mapper().readValue(file.toFile(), GeneratorProfile.class);
        if (profile.getId() == null || profile.getId().isBlank()) {
            profile.setId(IdUtil.newId());
        }
        return save(profile);
    }

    /**
     * 将方案导出为 JSON 文件。
     *
     * @param profile 待导出方案
     * @param file    目标文件路径
     */
    public void exportToFile(GeneratorProfile profile, Path file) throws Exception {
        StringListUtil.mapper().writerWithDefaultPrettyPrinter().writeValue(file.toFile(), profile);
    }

    /**
     * 将方案序列化为格式化的 JSON 字符串。
     *
     * @param profile 待序列化方案
     * @return JSON 文本
     */
    public String toJson(GeneratorProfile profile) throws Exception {
        return StringListUtil.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(profile);
    }

    /** 从 JSON 文件加载；文件不存在时创建 default 方案。 */
    private void load() {
        try {
            AppPaths.ensureAppHome();
            if (!Files.exists(AppPaths.profilesFile())) {
                GeneratorProfile defaultProfile = new GeneratorProfile();
                defaultProfile.setId(IdUtil.newId());
                defaultProfile.setName("default");
                profiles.add(defaultProfile);
                persist();
                return;
            }
            List<GeneratorProfile> loaded = StringListUtil.mapper().readValue(
                    AppPaths.profilesFile().toFile(),
                    new TypeReference<List<GeneratorProfile>>() {
                    }
            );
            profiles.clear();
            profiles.addAll(loaded);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load profiles.", e);
        }
    }

    /** 将当前方案列表写入 JSON 文件。 */
    private void persist() {
        try {
            AppPaths.ensureAppHome();
            StringListUtil.mapper().writerWithDefaultPrettyPrinter()
                    .writeValue(AppPaths.profilesFile().toFile(), profiles);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save profiles.", e);
        }
    }

    /** 通过 Jackson 序列化/反序列化实现深拷贝，避免引用共享。 */
    private GeneratorProfile copyProfile(GeneratorProfile source) {
        try {
            return StringListUtil.mapper().readValue(
                    StringListUtil.mapper().writeValueAsBytes(source),
                    GeneratorProfile.class
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to copy profile.", e);
        }
    }
}
