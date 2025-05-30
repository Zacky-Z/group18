package com.forbiddenisland.ui;

import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.IslandTile;
import com.forbiddenisland.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.util.Duration;

import java.util.List;

/**
 * Island Tile View - Displays the state of a single island tile
 */
public class TileView extends StackPane {

    private IslandTile tile;
    private Label nameLabel;
    private Label statusLabel;
    private VBox contentBox;
    private FlowPane pawnsPane;
    private Game game;

    private static final double TILE_SIZE = 120;
    private static final double PAWN_SIZE = 20;

    public TileView(IslandTile tile) {
        this(tile, null);
    }

    public TileView(IslandTile tile, Game game) {
        this.tile = tile;
        this.game = game;

        setPrefSize(TILE_SIZE, TILE_SIZE);
        setMinSize(TILE_SIZE, TILE_SIZE);
        setMaxSize(TILE_SIZE, TILE_SIZE);

        // Add shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0, 0, 0, 0.3));
        setEffect(dropShadow);

        setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(10), // More rounded corners
                new BorderWidths(2)
        )));

        contentBox = new VBox(5);
        contentBox.setPadding(new Insets(8));
        contentBox.setAlignment(Pos.CENTER);

        nameLabel = new Label();
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", 12));

        // Add pawn display area
        pawnsPane = new FlowPane();
        pawnsPane.setHgap(6);
        pawnsPane.setVgap(6);
        pawnsPane.setPadding(new Insets(5));
        pawnsPane.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(nameLabel, statusLabel, pawnsPane);
        getChildren().add(contentBox);

        // Add mouse hover effects
        setOnMouseEntered(e -> {
            // Slight zoom effect
            setScaleX(1.05);
            setScaleY(1.05);
            // Enhanced shadow
            dropShadow.setRadius(8.0);
            dropShadow.setOffsetX(4.0);
            dropShadow.setOffsetY(4.0);
        });

        setOnMouseExited(e -> {
            // Return to normal size
            setScaleX(1.0);
            setScaleY(1.0);
            // Return to normal shadow
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(3.0);
            dropShadow.setOffsetY(3.0);
        });

        update(tile);
    }

    /**
     * Update tile view
     * @param tile Island tile, null represents water
     */
    public void update(IslandTile tile) {
        this.tile = tile;

        if (tile == null) {
            // Water tile
            nameLabel.setText("Water");
            statusLabel.setText("");

            // Use gradient blue background for water
            setBackground(createWaterBackground());

            // Add water ripple effect
            InnerShadow innerShadow = new InnerShadow();
            innerShadow.setRadius(5.0);
            innerShadow.setColor(Color.color(0, 0, 0.5, 0.3));
            contentBox.setEffect(innerShadow);

            pawnsPane.getChildren().clear();
            return;
        }

        // Set tile name, add special marker for Fools' Landing
        if (tile.getName().equals("Fools' Landing")) {
            nameLabel.setText(tile.getName() + "\n(Helicopter Lift)");
            nameLabel.setTextFill(Color.DARKRED);
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        } else {
            nameLabel.setText(tile.getName());
            nameLabel.setTextFill(Color.BLACK);
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        }

        if (tile.isFlooded()) {
            // Flooded tile
            setBackground(createFloodedBackground());
            statusLabel.setText("Flooded");
            statusLabel.setTextFill(Color.DARKBLUE);

            // Add water ripple effect
            Lighting lighting = new Lighting();
            lighting.setDiffuseConstant(1.0);
            lighting.setSpecularConstant(0.0);
            lighting.setSpecularExponent(0.0);
            lighting.setSurfaceScale(5.0);

            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            lighting.setLight(light);

            contentBox.setEffect(lighting);
        } else {
            // Normal tile
            setBackground(createNormalBackground(tile));

            if (tile.getName().equals("Fools' Landing")) {
                statusLabel.setText("Helicopter Lift");
                statusLabel.setTextFill(Color.DARKRED);
            } else if (tile.getAssociatedTreasure() != null) {
                statusLabel.setText("Treasure: " + tile.getAssociatedTreasure().getDisplayName());
                statusLabel.setTextFill(Color.DARKGREEN);
            } else {
                statusLabel.setText("Normal");
                statusLabel.setTextFill(Color.BLACK);
            }

            // Remove special effects
            contentBox.setEffect(null);

            // Add special glow effect for Fools' Landing
            if (tile.getName().equals("Fools' Landing")) {
                DropShadow glow = new DropShadow();
                glow.setColor(Color.GOLD);
                glow.setWidth(20);
                glow.setHeight(20);
                glow.setRadius(10);
                contentBox.setEffect(glow);

                // Add pulse animation
                createPulseAnimation();
            }
        }

        // Update player pawns display
        updatePawns();
    }

    /**
     * Create water background
     */
    private Background createWaterBackground() {
        return new Background(new BackgroundFill(
                Color.rgb(100, 180, 255), // Brighter blue
                new CornerRadii(8),
                Insets.EMPTY
        ));
    }

    /**
     * Create flooded tile background
     */
    private Background createFloodedBackground() {
        return new Background(new BackgroundFill(
                Color.rgb(135, 206, 250), // Sky blue
                new CornerRadii(8),
                Insets.EMPTY
        ));
    }

    /**
     * Create normal tile background
     */
    private Background createNormalBackground(IslandTile tile) {
        Color backgroundColor;

        // Set different background colors based on treasure or Fools' Landing
        if (tile.getName().equals("Fools' Landing")) {
            // Fools' Landing uses special color
            backgroundColor = Color.rgb(255, 215, 0); // Gold
        } else if (tile.getAssociatedTreasure() != null) {
            // Treasure tiles use brighter green
            backgroundColor = Color.rgb(144, 238, 144); // Light green
        } else {
            backgroundColor = Color.rgb(152, 251, 152); // Pale green
        }

        return new Background(new BackgroundFill(
                backgroundColor,
                new CornerRadii(8),
                Insets.EMPTY
        ));
    }

    /**
     * Update player pawns display on the tile
     */
    private void updatePawns() {
        pawnsPane.getChildren().clear();

        if (game == null || tile == null || tile.getName() == null) return; // Ensure current tile is valid and has a name

        List<Player> players = game.getPlayers();
        for (Player player : players) {
            if (player.getPawn() != null && player.getPawn().getCurrentLocation() != null) {
                // Compare by tile name instead of object reference
                if (tile.getName().equals(player.getPawn().getCurrentLocation().getName())) {
                    createPawnCircle(player);
                }
            }
        }
    }

    /**
     * Create pawn graphic based on player
     */
    private void createPawnCircle(Player player) {
        String pawnColor = player.getPawn().getColor();

        // Get player number (extract number from player name)
        String playerName = player.getName();
        String playerNumber = playerName.replaceAll("\\D+", ""); // Extract numeric part

        // Default to 1.png, use corresponding image if valid number is found
        int pawnImageNumber = 1;
        try {
            pawnImageNumber = Integer.parseInt(playerNumber);
            // Ensure image number is between 1-7
            if (pawnImageNumber < 1 || pawnImageNumber > 7) {
                pawnImageNumber = 1;
            }
        } catch (NumberFormatException e) {
            // Use default value 1 if parsing fails
        }

        // Create pawn image
        String imagePath = "/images/pawns/" + pawnImageNumber + ".png";
        ImageView pawnImageView = null;

        try {
            Image pawnImage = new Image(getClass().getResourceAsStream(imagePath),
                    PAWN_SIZE * 2, PAWN_SIZE * 2, true, true);
            pawnImageView = new ImageView(pawnImage);

            // Add shadow effect
            DropShadow pawnShadow = new DropShadow();
            pawnShadow.setRadius(3.0);
            pawnShadow.setOffsetX(2.0);
            pawnShadow.setOffsetY(2.0);
            pawnShadow.setColor(Color.color(0, 0, 0, 0.5));
            pawnImageView.setEffect(pawnShadow);
        } catch (Exception e) {
            // Fall back to circle representation if image loading fails
            Circle circle = new Circle(PAWN_SIZE);

            Color fillColor;
            switch (pawnColor.toUpperCase()) {
                case "RED":
                    fillColor = Color.RED;
                    break;
                case "BLUE":
                    fillColor = Color.BLUE;
                    break;
                case "GREEN":
                    fillColor = Color.GREEN;
                    break;
                case "BLACK":
                    fillColor = Color.BLACK;
                    break;
                case "WHITE":
                    fillColor = Color.WHITE;
                    break;
                case "YELLOW":
                    fillColor = Color.YELLOW;
                    break;
                default:
                    fillColor = Color.GRAY;
            }

            circle.setFill(fillColor);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1.5);

            // Add shadow effect
            DropShadow pawnShadow = new DropShadow();
            pawnShadow.setRadius(3.0);
            pawnShadow.setOffsetX(2.0);
            pawnShadow.setOffsetY(2.0);
            pawnShadow.setColor(Color.color(0, 0, 0, 0.5));
            circle.setEffect(pawnShadow);

            pawnImageView = new ImageView();
            StackPane fallbackPane = new StackPane(circle);

            // Add player number text on the pawn
            Label numberLabel = new Label(playerNumber);
            numberLabel.setTextFill(pawnColor.equalsIgnoreCase("WHITE") || pawnColor.equalsIgnoreCase("YELLOW") ?
                    Color.BLACK : Color.WHITE);
            numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            fallbackPane.getChildren().add(numberLabel);

            pawnsPane.getChildren().add(fallbackPane);

            // Add player info tooltip
            fallbackPane.setOnMouseEntered(e2 -> {
                setTooltip(player.getName() + " (" + player.getRole().getChineseName() + ")");
                e2.consume();
            });

            fallbackPane.setOnMouseExited(e2 -> {
                clearTooltip();
                e2.consume();
            });

            return;
        }

        // Create container for pawn image
        StackPane pawnWithLabel = new StackPane();
        pawnWithLabel.getChildren().add(pawnImageView);

        // Add player info tooltip
        pawnWithLabel.setOnMouseEntered(e -> {
            setTooltip(player.getName() + " (" + player.getRole().getChineseName()+ ")");
            e.consume();
        });

        pawnWithLabel.setOnMouseExited(e -> {
            clearTooltip();
            e.consume();
        });

        pawnsPane.getChildren().add(pawnWithLabel);
    }

    private void setTooltip(String text) {
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(text);
        javafx.scene.control.Tooltip.install(this, tooltip);
    }

    private void clearTooltip() {
        javafx.scene.control.Tooltip.uninstall(this, null);
    }

    /**
     * Get the tile associated with this view
     */
    public IslandTile getTile() {
        return tile;
    }

    /**
     * Create pulse animation for Fools' Landing
     */
    private void createPulseAnimation() {
        // Create border pulse effect
        BorderStroke borderStroke = new BorderStroke(
                Color.GOLD,
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(3)
        );
        Border border = new Border(borderStroke);
        setBorder(border);

        // Create pulse animation
        DropShadow glow = (DropShadow) contentBox.getEffect();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(glow.radiusProperty(), 10),
                        new KeyValue(glow.colorProperty(), Color.GOLD)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(glow.radiusProperty(), 20),
                        new KeyValue(glow.colorProperty(), Color.ORANGE)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(glow.radiusProperty(), 10),
                        new KeyValue(glow.colorProperty(), Color.GOLD))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}