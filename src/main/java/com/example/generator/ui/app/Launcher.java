package com.example.generator.ui.app;

import javafx.application.Application;

/**
 * JavaFX 启动入口。
 * <p>
 * IntelliJ：使用 {@code Launcher} 运行配置，module-path 为 {@code D:/tools/Java/javafx-sdk-21.0.9/lib}。
 * Maven：{@code mvn javafx:run}。
 */
public final class Launcher {

    private Launcher() {
    }

    public static void main(String[] args) {
        Application.launch(GeneratorApp.class, args);
    }
}
