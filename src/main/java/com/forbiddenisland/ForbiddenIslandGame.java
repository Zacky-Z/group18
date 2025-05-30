package com.forbiddenisland;

import com.forbiddenisland.model.*;
import com.forbiddenisland.ui.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Forbidden Island Game main application class
 */
public class ForbiddenIslandGame extends Application {

    private Game game;
    private GameBoardView gameBoardView;
    private PlayerInfoPanel playerInfoPanel;
    private ActionPanel actionPanel;
    private StatusPanel statusPanel;
    private WaterMeterView waterMeterView;

    // Add a flag to prevent duplicate display of the game-over dialog
    private boolean isGameOver = false;

    @Override
    public void start(Stage primaryStage) {
        // Create the initial interface.
        createInitialScreen(primaryStage);
    }

    private void createInitialScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        
        // Use background image
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/background/forbidden_bg_3.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            //If the image fails to load, use a gradient blue background
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");
        }

        // Create title
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Label titleLabel = new Label("Forbidden Island");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.BLACK));
        
        Label subtitleLabel = new Label("Forbidden Island");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        subtitleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        root.setTop(titleBox);
        BorderPane.setAlignment(titleBox, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(titleBox, new Insets(0, 0, 50, 0));

        // Create buttons
        VBox buttonPanel = new VBox(20);
        buttonPanel.setAlignment(javafx.geometry.Pos.CENTER);
        buttonPanel.setPadding(new Insets(20));
        buttonPanel.setMaxWidth(300);
        buttonPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");

        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                             "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";
        String exitButtonStyle = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 16px; " +
                                "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";

        Button startButton = new Button("Start");
        startButton.setStyle(buttonStyle);
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setOnAction(e -> createDifficultyScreen(primaryStage));

        Button exitButton = new Button("Exit");
        exitButton.setStyle(exitButtonStyle);
        exitButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setOnAction(e -> primaryStage.close());

        buttonPanel.getChildren().addAll(startButton, exitButton);
        root.setCenter(buttonPanel);

        // Add a game introduction
        Label infoLabel = new Label("Forbidden Island is a cooperative board game where players work together to collect four treasures and evacuate all members from the island before it sinks.");
        infoLabel.setWrapText(true);
        infoLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        infoLabel.setFont(Font.font("Arial", 14));
        root.setBottom(infoLabel);
        BorderPane.setAlignment(infoLabel, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(infoLabel, new Insets(30, 0, 0, 0));

        // Set up the scene
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Forbidden Island Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void createDifficultyScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        
        // Use background image
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/background/forbidden_bg_3.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            // If the image fails to load, use a gradient blue background
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");
        }

        // Create title
        Label titleLabel = new Label("Select difficulty");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.BLACK));
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 30, 0));

        //Create a difficulty selection panel
        VBox difficultyPanel = new VBox(15);
        difficultyPanel.setPadding(new Insets(20));
        difficultyPanel.setAlignment(javafx.geometry.Pos.CENTER);
        difficultyPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");
        difficultyPanel.setMaxWidth(400);

        ToggleGroup difficultyGroup = new ToggleGroup();

        String radioStyle = "-fx-font-size: 16px; -fx-text-fill: white;";

        RadioButton noviceButton = new RadioButton("Begainer Lv.1");
        noviceButton.setStyle(radioStyle);
        noviceButton.setToggleGroup(difficultyGroup);
        noviceButton.setSelected(true);
        noviceButton.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);

        RadioButton normalButton = new RadioButton("Normal Lv.2");
        normalButton.setStyle(radioStyle);
        normalButton.setToggleGroup(difficultyGroup);

        RadioButton expertButton = new RadioButton("Elite Lv.3");
        expertButton.setStyle(radioStyle);
        expertButton.setToggleGroup(difficultyGroup);

        RadioButton legendButton = new RadioButton("Legendary Lv.4");
        legendButton.setStyle(radioStyle);
        legendButton.setToggleGroup(difficultyGroup);

        // Add difficulty descriptions
        Label descriptionLabel = new Label("The difficulty will impact the starting water level and the game process");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        // 按钮样式
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                            "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";
        
        Button startGameButton = new Button("Next");
        startGameButton.setStyle(buttonStyle);
        startGameButton.setMaxWidth(200);
        startGameButton.setOnAction(e -> {
            int difficulty = 1;
            if (normalButton.isSelected()) {
                difficulty = 2;
            } else if (expertButton.isSelected()) {
                difficulty = 3;
            } else if (legendButton.isSelected()) {
                difficulty = 4;
            }
            createRoleSelectionScreen(primaryStage, difficulty);
        });

        difficultyPanel.getChildren().addAll(
            descriptionLabel, 
            new javafx.scene.control.Separator(),
            noviceButton, normalButton, expertButton, legendButton, 
            new javafx.scene.control.Separator(),
            startGameButton
        );
        root.setCenter(difficultyPanel);

        // Set scene
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Select Difficulty - Forbidden Island Game");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void createRoleSelectionScreen(Stage primaryStage, int difficulty) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        
        // Use background image
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/background/forbidden_bg_3.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            //If the image fails to load, use a gradient blue background
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");
        }

        Label titleLabel = new Label("Character selection");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.BLACK));
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 30, 0));

        VBox selectionPanel = new VBox(15);
        selectionPanel.setPadding(new Insets(20));
        selectionPanel.setAlignment(javafx.geometry.Pos.CENTER);
        selectionPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");
        selectionPanel.setMaxWidth(400);

        ToggleGroup selectionGroup = new ToggleGroup();
        
        String radioStyle = "-fx-font-size: 16px; -fx-text-fill: white;";

        RadioButton randomButton = new RadioButton("Random(official)");
        randomButton.setStyle(radioStyle);
        randomButton.setToggleGroup(selectionGroup);
        randomButton.setSelected(true);

        RadioButton chooseButton = new RadioButton("Choose freely");
        chooseButton.setStyle(radioStyle);
        chooseButton.setToggleGroup(selectionGroup);
        
        // add introduction
        Label descriptionLabel = new Label("Choose a character assignment method. The official rules are random assignment, or you can manually select each player's character.");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        // button type
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                            "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";

        Button continueButton = new Button("Continue");
        continueButton.setStyle(buttonStyle);
        continueButton.setMaxWidth(200);
        continueButton.setOnAction(e -> {
            if (randomButton.isSelected()) {
                startGame(primaryStage, difficulty);
            } else {
                createPlayerRoleSelectionScreen(primaryStage, difficulty);
            }
        });

        selectionPanel.getChildren().addAll(
            descriptionLabel,
            new javafx.scene.control.Separator(),
            randomButton, chooseButton,
            new javafx.scene.control.Separator(),
            continueButton
        );
        root.setCenter(selectionPanel);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Select Character - Forbidden Island Game");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void createPlayerRoleSelectionScreen(Stage primaryStage, int difficulty) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Use Background Image
       try {
    Image bgImage = new Image(getClass().getResourceAsStream("/images/background/forbidden_bg_3.png"));
    BackgroundImage backgroundImage = new BackgroundImage(
        bgImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
    );
    root.setBackground(new Background(backgroundImage));
} catch (Exception e) {
    // If image loading fails, use a gradient blue background
    root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");
}

Label titleLabel = new Label("Select Character");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
titleLabel.setEffect(new javafx.scene.effect.DropShadow(5, javafx.scene.paint.Color.BLACK));
root.setTop(titleLabel);

VBox contentBox = new VBox(20);
contentBox.setPadding(new Insets(10));

// Create player name list
List<String> playerNames = Arrays.asList("Player 1", "Player 2", "Player 3", "Player 4");

// Create character selection combo boxes
List<AdventurerRole> availableRoles = new ArrayList<>(Arrays.asList(AdventurerRole.values()));
List<javafx.scene.control.ComboBox<AdventurerRole>> roleSelectors = new ArrayList<>();

for (int i = 0; i < playerNames.size(); i++) {
    HBox playerBox = new HBox(10);
    playerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

    Label playerLabel = new Label(playerNames.get(i) + ":");
    playerLabel.setMinWidth(60);

    javafx.scene.control.ComboBox<AdventurerRole> roleSelector = new javafx.scene.control.ComboBox<>();
    roleSelector.getItems().addAll(availableRoles);
    roleSelector.setValue(availableRoles.get(i % availableRoles.size())); // Default selection
    roleSelector.setMinWidth(200);

    // Set character description display
    roleSelector.setCellFactory(param -> new javafx.scene.control.ListCell<AdventurerRole>() {
        @Override
        protected void updateItem(AdventurerRole item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.name() + " - " + item.getDescription().split("\\(")[0].trim());
            }
        }
    });

    roleSelector.setButtonCell(new javafx.scene.control.ListCell<AdventurerRole>() {
        @Override
        protected void updateItem(AdventurerRole item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.name() + " - " + item.getDescription().split("\\(")[0].trim());
            }
        }
    });

    // When a role is selected, remove it from other combo boxes (prevent duplicate selection)
    roleSelector.setOnAction(e -> {
        AdventurerRole selectedRole = roleSelector.getValue();
        if (selectedRole != null) {
            for (javafx.scene.control.ComboBox<AdventurerRole> otherSelector : roleSelectors) {
                if (otherSelector != roleSelector && otherSelector.getValue() == selectedRole) {
                    // If another selector has the same role, find an available role to replace
                    List<AdventurerRole> availableForOther = new ArrayList<>(availableRoles);
                    for (javafx.scene.control.ComboBox<AdventurerRole> selector : roleSelectors) {
                        availableForOther.remove(selector.getValue());
                    }
                    if (!availableForOther.isEmpty()) {
                        otherSelector.setValue(availableForOther.get(0));
                    }
                }
            }
        }
    });

    roleSelectors.add(roleSelector);
    playerBox.getChildren().addAll(playerLabel, roleSelector);
    contentBox.getChildren().add(playerBox);
}

Button startGameButton = new Button("Start Game");
startGameButton.setOnAction(e -> {
    // Collect selected roles
    List<AdventurerRole> selectedRoles = new ArrayList<>();
    for (javafx.scene.control.ComboBox<AdventurerRole> selector : roleSelectors) {
        selectedRoles.add(selector.getValue());
    }

    // Initialize the game with selected roles
    initializeGameWithSelectedRoles(primaryStage, difficulty, playerNames, selectedRoles);
});

contentBox.getChildren().add(startGameButton);
root.setCenter(contentBox);

Scene scene = new Scene(root, 500, 400);
primaryStage.setTitle("Select Characters - Forbidden Island Game");
primaryStage.setScene(scene);
}

private void initializeGameWithSelectedRoles(Stage primaryStage, int difficulty, List<String> playerNames, List<AdventurerRole> selectedRoles) {
    // Create game instance and pass selected roles
    game = new Game(playerNames, difficulty, selectedRoles);
    setupMainGameUI(primaryStage);
}

private void startGame(Stage primaryStage, int difficulty) {
    // Create game instance (random role assignment)
    List<String> playerNames = Arrays.asList("Player 1", "Player 2", "Player 3", "Player 4"); // Default player names
    game = new Game(playerNames, difficulty); // Constructor for random roles
    setupMainGameUI(primaryStage);
}

private void setupMainGameUI(Stage primaryStage) {
    BorderPane gameLayout = new BorderPane();
    gameLayout.setPadding(new Insets(15));
    gameLayout.setStyle("-fx-background-color: #f0f8ff;"); // Add light blue background

    // Initialize UI components
        gameBoardView = new GameBoardView(game);
    statusPanel = new StatusPanel();
        playerInfoPanel = new PlayerInfoPanel(game);
    playerInfoPanel.setMainApp(this); // Set mainApp reference for PlayerInfoPanel
        actionPanel = new ActionPanel(game, this);
        actionPanel.setGameBoardView(gameBoardView);
    waterMeterView = new WaterMeterView(game.getWaterMeter());

    // Set minimum width to prevent panels from being squashed
    playerInfoPanel.setMinWidth(280);
    actionPanel.setMinWidth(280);
    waterMeterView.setMinWidth(200);

    // Add styles to panels
    playerInfoPanel.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
    actionPanel.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
    statusPanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ddd; -fx-border-radius: 5;");

    // Create right panel containing action panel and water meter
    VBox rightPanel = new VBox(15);
    rightPanel.getChildren().addAll(actionPanel, waterMeterView);

    // Layout UI components
    gameLayout.setCenter(gameBoardView);
    gameLayout.setLeft(playerInfoPanel);
    gameLayout.setRight(rightPanel);
    gameLayout.setBottom(statusPanel);

    // Set margins
    BorderPane.setMargin(gameBoardView, new Insets(10));
    BorderPane.setMargin(playerInfoPanel, new Insets(10));
    BorderPane.setMargin(rightPanel, new Insets(10));
    BorderPane.setMargin(statusPanel, new Insets(10, 0, 0, 0));

    // Menu bar
    HBox menuBar = new HBox(10);
    menuBar.setPadding(new Insets(10));
    menuBar.setStyle("-fx-background-color: #e6e6e6; -fx-border-color: #ccc; -fx-border-radius: 5;");
    menuBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

    // Beautify button styles
    String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
    String exitButtonStyle = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";

    Button restartButton = new Button("Restart Game");
    restartButton.setStyle(buttonStyle);
    restartButton.setOnAction(e -> restartGame());

    Button helpButton = new Button("Game Help");
    helpButton.setStyle(buttonStyle);
    helpButton.setOnAction(e -> showGameHelp());

    Button saveButton = new Button("Save Game");
    saveButton.setStyle(buttonStyle);
    saveButton.setOnAction(e -> saveGame(primaryStage));

    Button loadButton = new Button("Load Game");
    loadButton.setStyle(buttonStyle);
    loadButton.setOnAction(e -> loadGame(primaryStage));

    Button exitButton = new Button("Exit Game");
    exitButton.setStyle(exitButtonStyle);
    exitButton.setOnAction(e -> primaryStage.close());

    menuBar.getChildren().addAll(restartButton, helpButton, saveButton, loadButton, exitButton);
    gameLayout.setTop(menuBar);

    // Create scene and set larger initial size
    Scene scene = new Scene(gameLayout, 1400, 900);
    primaryStage.setTitle("Forbidden Island");
        primaryStage.setScene(scene);
    primaryStage.setResizable(true);

    // Set minimum window size to ensure UI elements aren't squashed
    primaryStage.setMinWidth(1200);
    primaryStage.setMinHeight(800);

    // Maximize window
    primaryStage.setMaximized(true);

    updateGameState(); // Initial UI update
}

/**
 * Update the game state and refresh the interface
     */
    public void updateGameState() {
    if (game == null) return;

    if (gameBoardView != null) gameBoardView.update();
    
    if (playerInfoPanel != null) playerInfoPanel.update();
    
    if (actionPanel != null) actionPanel.update();

    if (waterMeterView != null) waterMeterView.update();
    
    if (statusPanel != null) {
        statusPanel.setStatus(game.getCurrentPlayer().getName() + "'s turn. Water level: " + game.getWaterMeter().getWaterLevelLabel());
    }

    // Check game over conditions and determine the reason for ending
    if (!isGameOver && game.checkGameOverConditions()) {
        isGameOver = true; // Set flag to prevent duplicate dialogs
        String gameOverReason = determineGameOverReason();
        showGameOverDialog(false, "Game Over! Failed.", gameOverReason);
    } else if (!isGameOver && game.checkWinConditions()) {
        isGameOver = true; // Set flag to prevent duplicate dialogs
        showGameOverDialog(true, "Game Over! Victory!", "Congratulations! You successfully collected all treasures and evacuated the Forbidden Island with all members!");
    }
}

/**
 * Determine the specific reason for game over
 * @return Description of the game over reason
 */
private String determineGameOverReason() {
    System.out.println("Determining game over reason...");
    
    // Check if water level reached maximum
    if (game.getWaterMeter().hasReachedMaxLevel()) {
        String reason = "The water level has reached the maximum! The island is completely submerged.";
        System.out.println("Game over reason: " + reason);
        return reason;
    }
    
    // Check if Fools' Landing is sunk
    if (game.getIslandTileByName("Fools' Landing") == null) {
        String reason = "Fools' Landing has sunk! Unable to evacuate the island.";
        System.out.println("Game over reason: " + reason);
        return reason;
    }
    
    // Check if any treasure cannot be obtained
    for (Treasure treasure : game.getTreasures()) {
        if (!treasure.isCollected()) {
            int sunkTreasureTiles = 0;
            for (String tileName : treasure.getIslandTileNames()) {
                if (game.getIslandTileByName(tileName) == null) {
                    sunkTreasureTiles++;
                }
            }
            if (sunkTreasureTiles >= 2) {
                String reason = treasure.getType().getDisplayName() + "'s two tiles have both sunk! Unable to collect this treasure.";
                System.out.println("Game over reason: " + reason);
                return reason;
            }
        }
    }
    
    // Check if any player cannot move to a safe position
    for (Player player : game.getPlayers()) {
        if (player.getPawn().getCurrentLocation() == null) {
            String reason = player.getName() + "(" + player.getRole().getChineseName() + ") cannot move to a safe position!";
            System.out.println("Game over reason: " + reason);
            return reason;
        }
    }
    
    String reason = "Game ended for unknown reasons.";
    System.out.println("Game over reason: " + reason);
    return reason;
}

/**
 * Show game over dialog
 * @param isWin Whether it's a win
 * @param message Ending message
 * @param reason Specific reason for game over
 */
private void showGameOverDialog(boolean isWin, String message, String reason) {
    if (statusPanel != null) statusPanel.setStatus(message);
    if (actionPanel != null) actionPanel.disableAllButtons();

    // Create dialog directly without using Platform.runLater
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    alert.setTitle(isWin ? "Victory!" : "Game Over");
        alert.setHeaderText(message);

    // Create a text area to display detailed information
    javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea();
    textArea.setEditable(false);
    textArea.setWrapText(true);
    textArea.setPrefHeight(150);
    
    // Set dialog content including game over reason
    String content = reason + "\n\n";
    
    if (isWin) {
        content += "You successfully completed the challenge of the Forbidden Island!";
    } else {
        content += "The challenge of the Forbidden Island failed...";
    }
    
    textArea.setText(content);

    // Create a container for text and buttons
    javafx.scene.layout.VBox dialogContent = new javafx.scene.layout.VBox(10);
    dialogContent.getChildren().add(textArea);
    
    // Add buttons
    javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(10);
    buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
    
    javafx.scene.control.Button restartButton = new javafx.scene.control.Button("Restart");
    javafx.scene.control.Button exitButton = new javafx.scene.control.Button("Exit");
    
    restartButton.setOnAction(e -> {
        alert.close();
                restartGame();
    });
    
    exitButton.setOnAction(e -> {
        alert.close();
        Node anyNode = null;
        if (gameBoardView != null) anyNode = gameBoardView;
        else if (playerInfoPanel != null) anyNode = playerInfoPanel;
        else if (actionPanel != null) anyNode = actionPanel;
        else if (statusPanel != null) anyNode = statusPanel;

        if (anyNode != null && anyNode.getScene() != null && anyNode.getScene().getWindow() != null) {
            ((Stage) anyNode.getScene().getWindow()).close();
            } else {
            System.exit(0);
        }
    });
    
    buttonBox.getChildren().addAll(restartButton, exitButton);
    dialogContent.getChildren().add(buttonBox);
    
    // Set dialog content
    alert.getDialogPane().setContent(dialogContent);
    
    // Remove default buttons
    alert.getButtonTypes().clear();
    alert.getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);
    
    // Hide close button
    javafx.scene.Node closeButton = alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CLOSE);
    closeButton.setVisible(false);
    closeButton.setManaged(false);
    
    // Make dialog larger to display more content
    alert.getDialogPane().setPrefWidth(500);
    alert.getDialogPane().setPrefHeight(300);
    
    System.out.println("Showing game over dialog: " + message + " - " + reason);
    
    // Show dialog
    alert.show();
}

/**
 * Restart the game
     */
    private void restartGame() {
    // Clear all content from the stage and fully rebuild
        Stage primaryStage = (Stage) gameBoardView.getScene().getWindow();

    // Reset game over flag
    isGameOver = false;

    // Fully recreate game instance and UI components
        start(primaryStage);
    }

    /**
 * Show game help dialog
     */
    private void showGameHelp() {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);

        alert.setTitle("禁闭岛游戏帮助");
        alert.setHeaderText("游戏规则与操作说明");

        String content =
                "禁闭岛是一个合作类桌游，玩家们需要共同合作收集四件宝藏并全员撤离岛屿。\n\n" +
                        "游戏操作:\n" +
                        "1. 每个玩家每回合有3个行动点\n" +
                        "2. 点击板块可以选择要移动到的位置或要加固的板块\n" +
                        "3. 每个玩家有特殊能力，可以通过\"特殊能力\"按钮使用\n\n" +
                        "角色说明:\n" +
                        "- 飞行员: 可以飞到任意板块\n" +
                        "- 工程师: 可以一次加固两个板块\n" +
                        "- 领航员: 可以移动其他玩家\n" +
                        "- 潜水员: 可以穿过已淹没的板块\n" +
                        "- 信使: 可以给任何位置的玩家卡牌\n" +
                        "- 探险家: 可以斜向移动和加固\n\n" +
                        "游戏目标:\n" +
                        "收集全部四个宝藏并让所有玩家撤离到愚人号起飞点。";

        alert.setContentText(content);
        alert.showAndWait();
    }

private void saveGame(Stage primaryStage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("保存游戏");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game Save Files", "*.sav"));
    File file = fileChooser.showSaveDialog(primaryStage);
    if (file != null) {
        game.saveGame(file.getAbsolutePath());
    }
}

private void loadGame(Stage primaryStage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("加载游戏");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game Save Files", "*.sav"));
    File file = fileChooser.showOpenDialog(primaryStage);
    if (file != null) {
        game = GameLoader.loadGame(file.getAbsolutePath());
        if (game != null) {
            gameBoardView.setGame(game);
            playerInfoPanel.setGame(game);
            playerInfoPanel.setMainApp(this); // 设置PlayerInfoPanel的mainApp引用
            actionPanel.setGame(game);
            updateGameState();
        }
    }
}

public StatusPanel getStatusPanel() {
    return statusPanel;
}

/**
 * 获取玩家信息面板
 * @return 玩家信息面板
 */
public PlayerInfoPanel getPlayerInfoPanel() {
    return playerInfoPanel;
}

public void setGamePhase(Game.GamePhase phase) {
    if (game != null) {
        game.setCurrentPhase(phase);
        updateGameState();
    }
}
}
