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

/**
 * 游戏板视图 - 展示岛屿板块的布局和状态
 */
public class GameBoardView extends GridPane {
    
    private Game game;
    private TileView[][] tileViews;
    private static final int BOARD_SIZE = 6;
    
    // 板块选择回调
    private Consumer<IslandTile> tileSelectionCallback;
    private TileView selectedTileView;
    private Map<String, int[]> tileCoordinates;
    
    public GameBoardView(Game game) {
        this.game = game;
        this.tileViews = new TileView[BOARD_SIZE][BOARD_SIZE];
        this.tileCoordinates = new HashMap<>();
        
        // 设置游戏板外观
        setPadding(new Insets(15));
        setHgap(8);
        setVgap(8);
        
        // 添加背景
        setBackground(createGameBoardBackground());
        
        // 添加边框和阴影效果
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
     * 创建游戏板背景
     */
    private Background createGameBoardBackground() {
        // 尝试加载水纹背景图像
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
            // 如果图像加载失败，使用渐变蓝色背景
            return new Background(new BackgroundFill(
                    Color.rgb(70, 130, 180, 0.8), // 钢蓝色半透明
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
                    
                    // 添加点击事件
                    final int row = r;
                    final int col = c;
                    tileView.setOnMouseClicked(e -> handleTileClick(tileView, row, col));
                } else {
                    // 空位置显示为水
                    tileView = new TileView(null, game);
                    tileViews[r][c] = tileView;
                }
                
                // 使用StackPane包装TileView，以便添加额外的边距
                StackPane tileContainer = new StackPane(tileView);
                tileContainer.setPadding(new Insets(3));
                add(tileContainer, c, r);
            }
        }
    }
    
    /**
     * 设置板块选择回调
     * @param callback 当玩家选择板块时调用的回调函数
     */
    public void setTileSelectionCallback(Consumer<IslandTile> callback) {
        this.tileSelectionCallback = callback;
    }
    
    /**
     * 处理板块点击事件
     */
    private void handleTileClick(TileView tileView, int row, int col) {
        if (tileView.getTile() == null) return; // 忽略水域点击
        
        // 清除之前的选择
        clearSelection();
        
        // 设置新选择
        selectedTileView = tileView;
        tileView.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-border-radius: 8;");
        
        // 调用回调
        if (tileSelectionCallback != null) {
            tileSelectionCallback.accept(tileView.getTile());
        }
    }
    
    /**
     * 清除当前选择
     */
    public void clearSelection() {
        if (selectedTileView != null) {
            selectedTileView.setStyle("");
        }
    }
    
    /**
     * 获取当前选中的板块
     */
    public IslandTile getSelectedTile() {
        return selectedTileView != null ? selectedTileView.getTile() : null;
    }
    
    /**
     * 根据板块名称获取坐标
     */
    public int[] getTileCoordinates(String tileName) {
        return tileCoordinates.get(tileName);
    }
    
    /**
     * 更新游戏板视图
     */
    public void update() {
        IslandTile[][] gameBoard = game.getGameBoard();
        
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (tileViews[r][c] != null) {
                    IslandTile tile = gameBoard[r][c];
                    tileViews[r][c].update(tile);
                    
                    // 更新坐标映射
                    if (tile != null) {
                        tileCoordinates.put(tile.getName(), new int[]{r, c});
                    }
                }
            }
        }
    }
    
    /**
     * 设置游戏对象
     * @param game 新的游戏对象
     */
    public void setGame(Game game) {
        this.game = game;
        this.tileCoordinates.clear();
        clearSelection();
        
        // 清除现有棋盘
        getChildren().clear();
        
        // 重新初始化
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
                        
                        // 添加脉动动画效果
                        javafx.animation.ScaleTransition pulse = new javafx.animation.ScaleTransition(
                            javafx.util.Duration.millis(800), tileViews[r][c]);
                        pulse.setFromX(1.0);
                        pulse.setFromY(1.0);
                        pulse.setToX(1.05);
                        pulse.setToY(1.05);
                        pulse.setCycleCount(javafx.animation.Animation.INDEFINITE);
                        pulse.setAutoReverse(true);
                        pulse.play();
                        
                        // 存储动画以便稍后停止
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
                    // 停止动画
                    if (tileViews[r][c].getUserData() instanceof javafx.animation.Animation) {
                        ((javafx.animation.Animation) tileViews[r][c].getUserData()).stop();
                        tileViews[r][c].setUserData(null);
                    }
                    
                    // 重置缩放
                    tileViews[r][c].setScaleX(1.0);
                    tileViews[r][c].setScaleY(1.0);
                    
                    // 清除样式
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