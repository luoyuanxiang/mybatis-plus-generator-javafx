package com.example.generator.ui.util;

import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public final class AppPaths {

    private static final Path APP_HOME = Paths.get(System.getProperty("user.home"), ".mp-generator-ui");

    private AppPaths() {
    }

    public static Path appHome() {
        return APP_HOME;
    }

    public static Path dataSourcesFile() {
        return APP_HOME.resolve("datasources.json");
    }

    public static Path profilesFile() {
        return APP_HOME.resolve("profiles.json");
    }

    public static void ensureAppHome() {
        try {
            Files.createDirectories(APP_HOME);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create app home: " + APP_HOME, e);
        }
    }
}
