package com.forbiddenisland.ui.view;

import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.ui.util.AssetLoader;
import javafx.scene.layout.GridPane;

import java.util.List;

public class MapView extends GridPane {
    private List<IslandTile> islandTiles;
    private AssetLoader assetLoader;

    public MapView(List<IslandTile> islandTiles, AssetLoader assetLoader) {
        this.islandTiles = islandTiles;
        this.assetLoader = assetLoader;
        initializeMap();
    }

    private void initializeMap() {
        // 设置地图布局和样式
        setHgap(10);
        setVgap(10);
        setPadding(new javafx.geometry.Insets(10));

        // 绘制地图瓷砖
        drawTiles();
    }

    private void drawTiles() {
        // 绘制所有瓷砖
        for (IslandTile tile : islandTiles) {
            addTileToMap(tile);
        }
    }

    private void addTileToMap(IslandTile tile) {
        // 创建瓷砖UI元素并添加到地图
    }

    public void updateTileState(IslandTile tile) {
        // 更新特定瓷砖的状态显示
    }

    public void updateAllTiles() {
        // 更新所有瓷砖的状态显示
        getChildren().clear();
        drawTiles();
    }
}    