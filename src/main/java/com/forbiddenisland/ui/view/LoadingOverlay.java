package com.forbiddenisland.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoadingOverlay extends StackPane {
    private final VBox content;
    private final Label messageLabel;
    private final Circle spinner;
    private RotateTransition rotateTransition;
    private FadeTransition fadeTransition;
    
    public LoadingOverlay() {
        // i'll make a simple loading overlay with spinning animation
        content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        
        // create the spinner - this is just a circle that rotates
        spinner = new Circle(30);
        spinner.setFill(Color.TRANSPARENT);
        spinner.setStroke(Color.DODGERBLUE);
        spinner.setStrokeWidth(5);
        
        // create the loading message text
        messageLabel = new Label("Loading...");
        messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        
        // add components to the overlay
        content.getChildren().addAll(spinner, messageLabel);
        
        // setup the main container styling
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);"); // semi-transparent background
        getChildren().add(content);
        setVisible(false); // hidden by default
        
        // create the rotate animation for the spinner
        setupAnimations();
    }
    
    private void setupAnimations() {
        // create a rotation animation for the spinner
        rotateTransition = new RotateTransition(Duration.seconds(1.5), spinner);
        rotateTransition.setByAngle(360); // 360 degree rotation
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // repeat forever
        
        // create fade transition for smooth show/hide
        fadeTransition = new FadeTransition(Duration.millis(300), this);
    }
    
    // show the loading overlay with custom message
    public void show(String message) {
        Platform.runLater(() -> {
            // update message if provided
            if (message != null && !message.isEmpty()) {
                messageLabel.setText(message);
            } else {
                messageLabel.setText("Loading...");
            }
            
            // make sure we're visible
            setVisible(true);
            
            // start fade in and spinner animation
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
            
            rotateTransition.play();
        });
    }
    
    // hide the loading overlay
    public void hide() {
        Platform.runLater(() -> {
            // stop spinner animation
            rotateTransition.stop();
            
            // fade out then hide
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setOnFinished(e -> setVisible(false));
            fadeTransition.play();
        });
    }
    
    // show for a specific duration then auto-hide
    public void showTemporarily(String message, int durationMs) {
        show(message);
        
        // create a thread to hide the overlay after the specified duration
        Thread hideThread = new Thread(() -> {
            try {
                Thread.sleep(durationMs);
                hide();
            } catch (InterruptedException e) {
                // thread was interrupted, just hide immediately
                Platform.runLater(this::hide);
            }
        });
        
        // start as daemon thread
        hideThread.setDaemon(true);
        hideThread.start();
    }
} 