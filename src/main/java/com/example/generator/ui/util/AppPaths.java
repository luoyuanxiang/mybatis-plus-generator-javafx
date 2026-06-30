package com.example.generator.ui.util;

import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 应用本地存储路径常量。
 * <p>
 * 所有用户数据（数据源、配置方案）均保存在用户主目录下的 {@code .mp-generator-ui} 文件夹中。
 * </p>
 */
@UtilityClass
public class AppPaths {

    /** 应用数据根目录：{@code ~/.mp-generator-ui}。 */
    private static final Path APP_HOME = Paths.get(System.getProperty("user.home"), ".mp-generator-ui");

    /** @return 应用数据根目录路径 */
    public static Path appHome() {
        return APP_HOME;
    }

    /** @return 数据源列表 JSON 文件路径 */
    public static Path dataSourcesFile() {
        return APP_HOME.resolve("datasources.json");
    }

    /** @return 配置方案列表 JSON 文件路径 */
    public static Path profilesFile() {
        return APP_HOME.resolve("profiles.json");
    }

    /**
     * 确保应用数据目录存在，不存在则递归创建。
     *
     * @throws IllegalStateException 目录创建失败时抛出
     */
    public static void ensureAppHome() {
        try {
            Files.createDirectories(APP_HOME);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create app home: " + APP_HOME, e);
        }
    }
}
