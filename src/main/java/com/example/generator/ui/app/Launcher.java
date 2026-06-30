package com.example.generator.ui.app;

import javafx.application.Application;

/**
 * JavaFX 启动入口（JPMS 模块化应用）。
 * <p>
 * 模块：{@code com.example.generator.ui}，见 {@code module-info.java}。
 * 推荐 {@code mvn javafx:run} 或 {@code .\run.ps1}。
 */
public final class Launcher {

    private Launcher() {
    }

    public static void main(String[] args) {
        Application.launch(GeneratorApp.class, args);
    }
}
