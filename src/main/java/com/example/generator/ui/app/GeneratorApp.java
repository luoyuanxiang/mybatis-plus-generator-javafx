package com.example.generator.ui.app;

import com.example.generator.ui.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GeneratorApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 820);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());

        MainController controller = loader.getController();
        controller.setStage(stage);

        stage.setTitle("MyBatis-Plus Generator UI");
        stage.setScene(scene);
        stage.show();
    }
}
