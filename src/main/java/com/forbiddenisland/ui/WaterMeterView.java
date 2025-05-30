package com.forbiddenisland.ui;

import com.forbiddenisland.model.WaterMeter;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Water Meter visualization component
 * Displays current water level and corresponding number of flood cards to draw
 */
public class WaterMeterView extends VBox {
    
    private WaterMeter waterMeter;
    private Rectangle[] waterLevelRects;
    private Label waterLevelLabel;
    private Label cardsToDrawLabel;
    private StackPane waterContainer;
    private Rectangle waterFill;
    private int previousWaterLevel;
    
    // Maximum levels for the water meter
    private final int MAX_LEVELS = 10;
    
    // Color definitions for water levels
    private final Color[] LEVEL_COLORS = {
        Color.rgb(173, 216, 230),  // Light blue - Level 1
        Color.rgb(135, 206, 235),  // Sky blue - Level 2
        Color.rgb(100, 149, 237),  // Cornflower blue - Level 3
        Color.rgb(65, 105, 225),   // Royal blue - Level 4
        Color.rgb(0, 0, 255),      // Blue - Level 5
        Color.rgb(0, 0, 205),      // Medium blue - Level 6
        Color.rgb(0, 0, 139),      // Dark blue - Level 7
        Color.rgb(25, 25, 112),    // Midnight blue - Level 8
        Color.rgb(128, 0, 0),      // Maroon - Level 9
        Color.rgb(255, 0, 0)       // Red - Level 10
    };
    
    /**
     * Constructor
     * @param waterMeter Water meter model
     */
    public WaterMeterView(WaterMeter waterMeter) {
        this.waterMeter = waterMeter;
        this.previousWaterLevel = waterMeter.getCurrentWaterLevel();
        
        setPadding(new Insets(15));
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(
            Color.rgb(240, 240, 240), 
            new CornerRadii(10), 
            Insets.EMPTY
        )));
        
        setBorder(new Border(new BorderStroke(
            Color.GRAY, 
            BorderStrokeStyle.SOLID, 
            new CornerRadii(10), 
            new BorderWidths(2)
        )));
        
        setPrefWidth(200);
        
        createHeader();
        createWaterMeterVisual();
        createLabels();
        
        update();
    }
    
    /**
     * Create water meter header/title
     */
    private void createHeader() {
        Label titleLabel = new Label("Water Meter");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        getChildren().add(titleLabel);
    }
    
    /**
     * Create visual components for water meter
     */
    private void createWaterMeterVisual() {
        // Create water meter container
        waterContainer = new StackPane();
        waterContainer.setPrefSize(160, 300);
        waterContainer.setMaxSize(160, 300);
        waterContainer.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: white;");
        
        // Create water fill
        waterFill = new Rectangle(156, 0);
        waterFill.setFill(Color.BLUE);
        waterFill.setTranslateY(150); // Initial position at bottom
        
        // Create water level markers
        VBox levelsBox = new VBox(5);
        levelsBox.setPadding(new Insets(5));
        levelsBox.setAlignment(Pos.BOTTOM_CENTER);
        
        waterLevelRects = new Rectangle[MAX_LEVELS];
        
        for (int i = MAX_LEVELS - 1; i >= 0; i--) {
            HBox levelRow = new HBox(5);
            levelRow.setAlignment(Pos.CENTER_LEFT);
            
            Rectangle levelRect = new Rectangle(20, 20);
            levelRect.setFill(LEVEL_COLORS[i]);
            levelRect.setStroke(Color.BLACK);
            levelRect.setStrokeWidth(1);
            waterLevelRects[i] = levelRect;
            
            Text levelText = new Text(String.valueOf(i + 1));
            levelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            
            Text cardsText = new Text("(" + getCardsForLevel(i + 1) + " flood cards)");
            cardsText.setFont(Font.font("Arial", 10));
            
            levelRow.getChildren().addAll(levelRect, levelText, cardsText);
            levelsBox.getChildren().add(levelRow);
        }
        
        waterContainer.getChildren().addAll(waterFill, levelsBox);
        getChildren().add(waterContainer);
    }
    
    /**
     * Get number of flood cards to draw for specific level
     */
    private int getCardsForLevel(int level) {
        int[] cardLevels = {2, 2, 3, 3, 4, 4, 5, 5, 6, 10};
        if (level > 0 && level <= cardLevels.length) {
            return cardLevels[level - 1];
        }
        return 0;
    }
    
    /**
     * Create labels
     */
    private void createLabels() {
        waterLevelLabel = new Label("Current Level: 1");
        waterLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        cardsToDrawLabel = new Label("Draw Flood Cards: 2");
        cardsToDrawLabel.setFont(Font.font("Arial", 14));
        
        getChildren().addAll(waterLevelLabel, cardsToDrawLabel);
    }
    
    /**
     * Update water meter display
     */
    public void update() {
        if (waterMeter == null) {
            System.err.println("WaterMeterView: waterMeter is null, cannot update");
            return;
        }
        
        int currentLevel = waterMeter.getCurrentWaterLevel();
        System.out.println("WaterMeterView: Updating water meter display. Current level: " + currentLevel + 
                           ", Previous level: " + previousWaterLevel);
        
        // Check if water level changed
        boolean waterLevelIncreased = currentLevel > previousWaterLevel;
        
        // Update labels
        waterLevelLabel.setText("Current Level: " + currentLevel + " - " + waterMeter.getWaterLevelLabel());
        cardsToDrawLabel.setText("Draw Flood Cards: " + waterMeter.getNumberOfFloodCardsToDraw());
        
        // Highlight current water level
        for (int i = 0; i < MAX_LEVELS; i++) {
            if (i < currentLevel) {
                waterLevelRects[i].setOpacity(1.0);
                waterLevelRects[i].setStroke(Color.YELLOW);
                waterLevelRects[i].setStrokeWidth(2);
            } else {
                waterLevelRects[i].setOpacity(0.5);
                waterLevelRects[i].setStroke(Color.BLACK);
                waterLevelRects[i].setStrokeWidth(1);
            }
        }
        
        // Update water fill height
        double fillHeight = (currentLevel * 1.0 / MAX_LEVELS) * 300;
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), waterFill);
        transition.setToY(150 - fillHeight/2);
        waterFill.setHeight(fillHeight);
        
        // Set color based on water level
        waterFill.setFill(LEVEL_COLORS[currentLevel - 1]);
        
        // Add warning blink effect if level is dangerous
        if (currentLevel >= 7) {
            FadeTransition fade = new FadeTransition(Duration.millis(500), waterLevelLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.3);
            fade.setCycleCount(6);
            fade.setAutoReverse(true);
            fade.play();
            
            waterLevelLabel.setTextFill(Color.RED);
        } else {
            waterLevelLabel.setTextFill(Color.BLACK);
        }
        
        // Add animation effect if water level increased
        if (waterLevelIncreased) {
            System.out.println("WaterMeterView: Water level increased from " + previousWaterLevel + " to " + currentLevel);
            playWaterLevelIncreaseAnimation(currentLevel);
            
            // Show water level increase warning
            if (currentLevel >= 7) {
                showHighWaterLevelWarning(currentLevel);
            }
        }
        
        transition.play();
        
        // Update previous water level
        previousWaterLevel = currentLevel;
    }
    
    /**
     * Play water level increase animation
     */
    private void playWaterLevelIncreaseAnimation(int currentLevel) {
        // Animate rectangle for current level
        Rectangle currentLevelRect = waterLevelRects[currentLevel - 1];
        
        // Create scale animation
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), currentLevelRect);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        
        // Create color blink animation
        Timeline colorTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(currentLevelRect.fillProperty(), LEVEL_COLORS[currentLevel - 1])),
            new KeyFrame(Duration.millis(150), 
                new KeyValue(currentLevelRect.fillProperty(), Color.YELLOW)),
            new KeyFrame(Duration.millis(300), 
                new KeyValue(currentLevelRect.fillProperty(), LEVEL_COLORS[currentLevel - 1]))
        );
        colorTimeline.setCycleCount(2);
        
        // Run animations in parallel
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, colorTimeline);
        parallelTransition.play();
        
        // Water fill wave effect
        Timeline waterWaveTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight())),
            new KeyFrame(Duration.millis(200), 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight() + 20)),
            new KeyFrame(Duration.millis(400), 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight() - 10)),
            new KeyFrame(Duration.millis(600), 
                new KeyValue(waterFill.heightProperty(), waterFill.getHeight()))
        );
        waterWaveTimeline.play();
    }
    
    /**
     * Show high water level warning
     */
    private void showHighWaterLevelWarning(int currentLevel) {
        String warningMessage;
        
        if (currentLevel == 10) {
            warningMessage = "Danger! Water level has reached maximum! Game will end soon!";
        } else if (currentLevel == 9) {
            warningMessage = "Warning! Water level has reached dangerous level! Draw 6 flood cards per turn!";
        } else if (currentLevel == 8) {
            warningMessage = "Warning! Water level has reached alert level! Draw 5 flood cards per turn!";
        } else if (currentLevel == 7) {
            warningMessage = "Attention! Water level has risen to high level! Draw 5 flood cards per turn!";
        } else {
            return; // No warning
        }
        
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Water Level Warning");
        alert.setHeaderText("Water level has risen to " + currentLevel + "!");
        alert.setContentText(warningMessage);
        alert.show();
    }
    
    /**
     * Set water meter model
     * @param waterMeter New water meter model
     */
    public void setWaterMeter(WaterMeter waterMeter) {
        this.waterMeter = waterMeter;
        this.previousWaterLevel = waterMeter.getCurrentWaterLevel();
        update();
    }
}