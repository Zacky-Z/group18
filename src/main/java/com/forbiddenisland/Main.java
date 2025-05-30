package com.forbiddenisland;

import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.ui.controller.GameControllerFX;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the Forbidden Island game.
 * Initializes the game controller and UI components.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create game controller
        GameController gameController = new GameController();
        
        // Create JavaFX controller and start game
        GameControllerFX controllerFX = new GameControllerFX(gameController, primaryStage);
        controllerFX.startGame();
    }
}    