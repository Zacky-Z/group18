package com.forbiddenisland;

import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.ui.controller.GameControllerFX;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 创建游戏控制器
        GameController gameController = new GameController();
        
        // 创建JavaFX控制器并启动游戏
        GameControllerFX controllerFX = new GameControllerFX(gameController, primaryStage);
        controllerFX.startGame();
    }
}    