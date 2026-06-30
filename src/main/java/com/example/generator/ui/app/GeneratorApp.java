package com.example.generator.ui.app;

import com.example.generator.ui.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX 应用程序主类。
 * <p>
 * 加载 {@code main-view.fxml} 布局，绑定 {@link MainController}，
 * 并应用 {@code app.css} 样式表。由 {@link Launcher} 或 {@code mvn javafx:run} 启动。
 * </p>
 */
public class GeneratorApp extends Application {

    /** 主窗口默认宽度（像素）。 */
    private static final int DEFAULT_WIDTH = 1280;

    /** 主窗口默认高度（像素）。 */
    private static final int DEFAULT_HEIGHT = 820;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
        Scene scene = new Scene(loader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());

        MainController controller = loader.getController();
        controller.setStage(stage);

        stage.setTitle("代码生成器");
        stage.setScene(scene);
        stage.show();
    }
}
