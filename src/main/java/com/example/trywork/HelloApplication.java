package com.example.trywork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main application class for the Forbidden Island game.
 * Handles game initialization and main window setup (Week 12).
 */
public class HelloApplication extends Application {
    private static final double WINDOW_WIDTH = 1024;
    private static final double WINDOW_HEIGHT = 768;
    private static final String WINDOW_TITLE = "Forbidden Island";
    private static final String FXML_PATH = "hello-view.fxml";

    @Override
    public void start(Stage stage) throws IOException {
        // Load the main game interface
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(FXML_PATH));
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Configure the main window
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);  // Lock window size for consistent layout
        
        // Apply game styles
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        // Show the game window
        stage.show();
    }

    /**
     * Application entry point.
     * Launches the JavaFX application thread.
     */
    public static void main(String[] args) {
        launch();
    }
}