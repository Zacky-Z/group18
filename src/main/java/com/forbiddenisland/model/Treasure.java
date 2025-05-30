package com.forbiddenisland.model;

import java.util.List;
import java.io.Serializable;

/**
 * Represents a treasure that can be collected in the game.
 * 代表游戏中可以收集的宝藏。
 */
public class Treasure implements Serializable{
    private TreasureType type; // The type of the treasure (宝藏的类型)
    private List<String> islandTileNames; // Names of the IslandTiles where this treasure can be captured (可以获取此宝藏的岛屿板块名称列表)
    private boolean collected; // Whether this treasure has been collected by any player (此宝藏是否已被任何玩家收集)

    /**
     * Constructor for Treasure.
     * Treasure 的构造函数。
     * @param type The type of the treasure. (宝藏的类型)
     * @param islandTileNames A list of names of the IslandTiles associated with this treasure. (与此宝藏关联的岛屿板块名称列表)
     */
    public Treasure(TreasureType type, List<String> islandTileNames) {
        this.type = type;
        this.islandTileNames = islandTileNames;
        this.collected = false; // Treasures start uncollected (宝藏初始未被收集)
    }

    /**
     * Gets the type of the treasure.
     * 获取宝藏的类型。
     * @return The TreasureType. (宝藏类型)
     */
    public TreasureType getType() {
        return type;
    }

    /**
     * Gets the names of the IslandTiles where this treasure can be captured.
     * 获取可以捕获此宝藏的岛屿板块的名称。
     * @return A list of island tile names. (岛屿板块名称列表)
     */
    public List<String> getIslandTileNames() {
        return islandTileNames;
    }

    /**
     * Checks if this treasure has been collected.
     * 检查此宝藏是否已被收集。
     * @return true if collected, false otherwise. (如果已收集则为 true，否则为 false)
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Marks this treasure as collected.
     * 将此宝藏标记为已收集。
     */
    public void setCollected() {
        this.collected = true;
    }
} 