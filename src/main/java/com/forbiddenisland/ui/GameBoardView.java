package com.forbiddenisland.ui;

import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.IslandTile;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
        
        setPadding(new Insets(10));
        setHgap(5);
        setVgap(5);
        
        initializeBoard();
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
                
                StackPane tileContainer = new StackPane(tileView);
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
        tileView.setStyle("-fx-border-color: red; -fx-border-width: 3;");
        
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
            selectedTileView = null;
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
} 