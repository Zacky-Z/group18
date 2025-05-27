package com.forbiddenisland.ui.view;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.enums.TurnPhase;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StatusBar extends HBox {
    private Label currentPlayerLabel;
    private Label phaseLabel;
    private Label actionsLabel;
    private ProgressBar actionProgress;
    private Label waterLevelLabel;
    private Label statusMessageLabel;
    
    // constants for styling
    private static final String STATUS_BAR_STYLE = "-fx-background-color: #333333; -fx-padding: 5;";
    private static final String LABEL_STYLE = "-fx-text-fill: white; -fx-font-weight: bold;";
    private static final String MESSAGE_STYLE = "-fx-text-fill: #FFDD00; -fx-font-style: italic;";
    
    public StatusBar() {
        initializeComponents();
    }
    
    private void initializeComponents() {
        // setup the main container
        setStyle(STATUS_BAR_STYLE);
        setPadding(new Insets(5));
        setSpacing(15);
        
        // player info section
        VBox playerInfoBox = new VBox(5);
        currentPlayerLabel = new Label("Current Player: None");
        currentPlayerLabel.setStyle(LABEL_STYLE);
        phaseLabel = new Label("Phase: Setup");
        phaseLabel.setStyle(LABEL_STYLE);
        playerInfoBox.getChildren().addAll(currentPlayerLabel, phaseLabel);
        
        // action progress section
        VBox actionBox = new VBox(5);
        actionsLabel = new Label("Actions: 0/3");
        actionsLabel.setStyle(LABEL_STYLE);
        actionProgress = new ProgressBar(1.0);
        actionProgress.setPrefWidth(150);
        actionBox.getChildren().addAll(actionsLabel, actionProgress);
        
        // water level indicator
        VBox waterBox = new VBox(5);
        waterLevelLabel = new Label("Water Level: 1");
        waterLevelLabel.setStyle(LABEL_STYLE);
        waterBox.getChildren().add(waterLevelLabel);
        
        // status message area - expands to fill available space
        VBox messageBox = new VBox(5);
        HBox.setHgrow(messageBox, Priority.ALWAYS);
        statusMessageLabel = new Label("Welcome to Forbidden Island!");
        statusMessageLabel.setStyle(MESSAGE_STYLE);
        messageBox.getChildren().add(statusMessageLabel);
        
        // add all sections to the status bar
        getChildren().addAll(playerInfoBox, actionBox, waterBox, messageBox);
    }
    
    // update the status bar with current game information
    public void updateStatus(Adventurer currentPlayer, TurnPhase phase, int actionsRemaining, int maxActions, int waterLevel) {
        // make sure ui updates happen on the javafx application thread
        Platform.runLater(() -> {
            // update player info
            if (currentPlayer != null) {
                currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
                
                // set player color based on adventurer type
                switch (currentPlayer.getType()) {
                    case DIVER -> currentPlayerLabel.setTextFill(Color.BLUE);
                    case EXPLORER -> currentPlayerLabel.setTextFill(Color.GREEN);
                    case ENGINEER -> currentPlayerLabel.setTextFill(Color.RED);
                    case PILOT -> currentPlayerLabel.setTextFill(Color.BLACK);
                    case NAVIGATOR -> currentPlayerLabel.setTextFill(Color.YELLOW);
                    case MESSENGER -> currentPlayerLabel.setTextFill(Color.GRAY);
                    default -> currentPlayerLabel.setTextFill(Color.WHITE);
                }
            } else {
                currentPlayerLabel.setText("Current Player: None");
                currentPlayerLabel.setTextFill(Color.WHITE);
            }
            
            // update phase info
            phaseLabel.setText("Phase: " + formatPhase(phase));
            
            // update action progress
            actionsLabel.setText("Actions: " + actionsRemaining + "/" + maxActions);
            double progress = (double) actionsRemaining / maxActions;
            actionProgress.setProgress(progress);
            
            // change progress bar color based on remaining actions
            if (progress > 0.66) {
                actionProgress.setStyle("-fx-accent: green;");
            } else if (progress > 0.33) {
                actionProgress.setStyle("-fx-accent: orange;");
            } else {
                actionProgress.setStyle("-fx-accent: red;");
            }
            
            // update water level
            waterLevelLabel.setText("Water Level: " + waterLevel);
            
            // change water level text color based on danger level
            if (waterLevel <= 2) {
                waterLevelLabel.setTextFill(Color.LIGHTBLUE);
            } else if (waterLevel <= 4) {
                waterLevelLabel.setTextFill(Color.BLUE);
            } else {
                waterLevelLabel.setTextFill(Color.DARKBLUE);
            }
        });
    }
    
    // display a status message to the user
    public void showMessage(String message) {
        Platform.runLater(() -> statusMessageLabel.setText(message));
    }
    
    // display a temporary status message that disappears after a delay
    public void showTemporaryMessage(String message, int durationMs) {
        Platform.runLater(() -> {
            // store original message to restore later
            final String originalMessage = statusMessageLabel.getText();
            
            // show the temporary message
            statusMessageLabel.setText(message);
            
            // create a thread to restore the original message after the delay
            Thread restoreThread = new Thread(() -> {
                try {
                    Thread.sleep(durationMs);
                    Platform.runLater(() -> statusMessageLabel.setText(originalMessage));
                } catch (InterruptedException e) {
                    // thread was interrupted, not a big deal for our purposes
                }
            });
            
            // start the thread (use daemon thread to not block application shutdown)
            restoreThread.setDaemon(true);
            restoreThread.start();
        });
    }
    
    // format the turn phase for display
    private String formatPhase(TurnPhase phase) {
        if (phase == null) return "Setup";
        
        // convert enum name to a nicer format (ACTION -> Action)
        String phaseName = phase.name();
        return phaseName.charAt(0) + phaseName.substring(1).toLowerCase().replace('_', ' ');
    }
} 