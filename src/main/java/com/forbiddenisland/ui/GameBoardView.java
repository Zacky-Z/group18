package com.forbiddenisland.ui;

import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.IslandTile;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.Set;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

/**
 * Game board view - displays the layout and state of island tiles
 */
public class GameBoardView extends GridPane {

    private Game game;
    private TileView[][] tileViews;
    private static final int BOARD_SIZE = 6;

    // Tile selection callback
    private Consumer<IslandTile> tileSelectionCallback;
    private TileView selectedTileView;
    private Map<String, int[]> tileCoordinates;

    public GameBoardView(Game game) {
        this.game = game;
        this.tileViews = new TileView[BOARD_SIZE][BOARD_SIZE];
        this.tileCoordinates = new HashMap<>();

        // Set game board appearance
        setPadding(new Insets(15));
        setHgap(8);
        setVgap(8);

        // Add background
        setBackground(createGameBoardBackground());

        // Add border and shadow effect
        setBorder(new Border(new BorderStroke(
                Color.rgb(0, 0, 100, 0.5),
                BorderStrokeStyle.SOLID,
                new CornerRadii(15),
                new BorderWidths(3)
        )));

        DropShadow boardShadow = new DropShadow();
        boardShadow.setRadius(10.0);
        boardShadow.setOffsetX(3.0);
        boardShadow.setOffsetY(3.0);
        boardShadow.setColor(Color.color(0, 0, 0, 0.5));
        setEffect(boardShadow);

        initializeBoard();
    }

    /**
     * Create game board background
     */
    private Background createGameBoardBackground() {
        // Attempt to load water texture background image
        try {
            Image waterImage = new Image(getClass().getResourceAsStream("/images/background/forbidden_bg_3.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                    waterImage,
                    BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT
            );
            return new Background(backgroundImage);
        } catch (Exception e) {
            // Use gradient blue background if image loading fails
            return new Background(new BackgroundFill(
                    Color.rgb(70, 130, 180, 0.8), // Steel blue semi-transparent
                    new CornerRadii(15),
                    Insets.EMPTY
            ));
        }
    }

    private void initializeBoard() {
        IslandTile[][] gameBoard = game.getGameBoard();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                IslandTile tile = gameBoard[r][c];
                TileView tileView;

                if (tile != null) {
                    tileView = new TileView(tile, game);
                    tileViews[r][c] = tileView;
                    tileCoordinates.put(tile.getName(), new int[]{r, c});

                    // Add click event
                    final int row = r;
                    final int col = c;
                    tileView.setOnMouseClicked(e -> handleTileClick(tileView, row, col));
                } else {
                    // Empty position shown as water
                    tileView = new TileView(null, game);
                    tileViews[r][c] = tileView;
                }

                // Wrap TileView with StackPane for additional margins
                StackPane tileContainer = new StackPane(tileView);
                tileContainer.setPadding(new Insets(3));
                add(tileContainer, c, r);
            }
        }
    }

    /**
     * Set tile selection callback
     * @param callback Callback function to invoke when a tile is selected by the player
     */
    public void setTileSelectionCallback(Consumer<IslandTile> callback) {
        this.tileSelectionCallback = callback;
    }

    /**
     * Handle tile click event
     */
    private void handleTileClick(TileView tileView, int row, int col) {
        if (tileView.getTile() == null) return; // Ignore water clicks

        // Clear previous selection
        clearSelection();

        // Set new selection
        selectedTileView = tileView;
        tileView.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-border-radius: 8;");

        // Invoke callback
        if (tileSelectionCallback != null) {
            tileSelectionCallback.accept(tileView.getTile());
        }
    }

    /**
     * Clear current selection
     */
    public void clearSelection() {
        if (selectedTileView != null) {
            selectedTileView.setStyle("");
        }
    }

    /**
     * Get currently selected tile
     */
    public IslandTile getSelectedTile() {
        return selectedTileView != null ? selectedTileView.getTile() : null;
    }

    /**
     * Get coordinates of a tile by its name
     */
    public int[] getTileCoordinates(String tileName) {
        return tileCoordinates.get(tileName);
    }

    /**
     * Update the game board view
     */
    public void update() {
        IslandTile[][] gameBoard = game.getGameBoard();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (tileViews[r][c] != null) {
                    IslandTile tile = gameBoard[r][c];
                    tileViews[r][c].update(tile);

                    // Update coordinate mapping
                    if (tile != null) {
                        tileCoordinates.put(tile.getName(), new int[]{r, c});
                    }
                }
            }
        }
    }

    /**
     * Set game object
     * @param game New game object
     */
    public void setGame(Game game) {
        this.game = game;
        this.tileCoordinates.clear();
        clearSelection();

        // Clear existing board
        getChildren().clear();

        // Re-initialize
        initializeBoard();
    }

    /**
     * Highlights a set of tiles on the board.
     * @param tilesToHighlight The set of IslandTile objects to highlight.
     * @param color The color to use for highlighting.
     */
    public void highlightTiles(Set<IslandTile> tilesToHighlight, Color color) {
        clearSelectionHighlights(); // Clear previous highlights first
        if (tilesToHighlight == null) return;

        String borderColor = colorToHex(color);
        String highlightStyle = "-fx-border-color: " + borderColor + "; -fx-border-width: 3; -fx-border-radius: 8; -fx-effect: dropshadow(three-pass-box, " + borderColor + ", 10, 0.5, 0, 0);";

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (tileViews[r][c] != null && tileViews[r][c].getTile() != null) {
                    if (tilesToHighlight.contains(tileViews[r][c].getTile())) {
                        tileViews[r][c].setStyle(highlightStyle);

                        // Add pulsing animation effect
                        ScaleTransition pulse = new ScaleTransition(
                                Duration.millis(800), tileViews[r][c]);
                        pulse.setFromX(1.0);
                        pulse.setFromY(1.0);
                        pulse.setToX(1.05);
                        pulse.setToY(1.05);
                        pulse.setCycleCount(javafx.animation.Animation.INDEFINITE);
                        pulse.setAutoReverse(true);
                        pulse.play();

                        // Store animation for later stopping
                        tileViews[r][c].setUserData(pulse);
                    }
                }
            }
        }
    }

    /**
     * Clears all visual highlights from all tiles.
     * This is different from clearSelection() which only clears the single-tile red selection border.
     */
    public void clearSelectionHighlights() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (tileViews[r][c] != null) {
                    // Stop animation
                    if (tileViews[r][c].getUserData() instanceof javafx.animation.Animation) {
                        ((javafx.animation.Animation) tileViews[r][c].getUserData()).stop();
                        tileViews[r][c].setUserData(null);
                    }

                    // Reset scale
                    tileViews[r][c].setScaleX(1.0);
                    tileViews[r][c].setScaleY(1.0);

                    // Clear styles
                    tileViews[r][c].setStyle(""); // Reset any custom style
                }
            }
        }
        // Also ensure the single red selection is cleared if it was active
        if (selectedTileView != null) {
            selectedTileView.setStyle("");
            selectedTileView = null;
        }
    }

    // Helper to convert JavaFX Color to CSS hex string
    private String colorToHex(Color color) {
        return String.format("#%02x%02x%02x",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}