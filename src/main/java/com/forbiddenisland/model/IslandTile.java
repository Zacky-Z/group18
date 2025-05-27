package com.forbiddenisland.model;

/**
 * Represents an island tile in the game.
 * 代表游戏中的一个岛屿板块。
 */
public class IslandTile {

    private String name; // Name of the island tile (板块名称)
    private boolean isFlooded; // Whether the tile is flooded (板块是否被淹没)
    private TreasureType associatedTreasure; // Treasure associated with this tile, if any (与此板块关联的宝藏，如果有)
    private boolean isStartingTileForPlayer; // Whether this tile is a starting position for a player (此板块是否为玩家的起始位置)

    /**
     * Constructor for IslandTile.
     * IslandTile 的构造函数。
     * @param name The name of the island tile. (岛屿板块的名称)
     */
    public IslandTile(String name) {
        this.name = name;
        this.isFlooded = false; // Tiles start unflooded (板块初始未被淹没)
        this.associatedTreasure = null; // No treasure by default (默认没有宝藏)
        this.isStartingTileForPlayer = false; // Not a starting tile by default (默认不是起始板块)
    }

    /**
     * Gets the name of the island tile.
     * 获取岛屿板块的名称。
     * @return The name of the tile. (板块的名称)
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the tile is flooded.
     * 检查板块是否被淹没。
     * @return true if flooded, false otherwise. (如果被淹没则为 true，否则为 false)
     */
    public boolean isFlooded() {
        return isFlooded;
    }

    /**
     * Sets the flooded state of the tile.
     * 设置板块的淹没状态。
     * @param flooded The new flooded state. (新的淹没状态)
     */
    public void setFlooded(boolean flooded) {
        this.isFlooded = flooded;
    }

    /**
     * Flips the tile to its flooded side.
     * 将板块翻到其淹没的一面。
     */
    public void flood() {
        this.isFlooded = true;
    }

    /**
     * Flips the tile to its unflooded side (shores it up).
     * 将板块翻到其未淹没的一面（填补）。
     */
    public void shoreUp() {
        this.isFlooded = false;
    }

    /**
     * Gets the treasure associated with this tile.
     * 获取与此板块关联的宝藏。
     * @return The type of treasure, or null if no treasure is associated. (宝藏类型，如果没有关联宝藏则为 null)
     */
    public TreasureType getAssociatedTreasure() {
        return associatedTreasure;
    }

    /**
     * Sets the treasure associated with this tile.
     * 设置与此板块关联的宝藏。
     * @param associatedTreasure The type of treasure to associate. (要关联的宝藏类型)
     */
    public void setAssociatedTreasure(TreasureType associatedTreasure) {
        this.associatedTreasure = associatedTreasure;
    }

    /**
     * Checks if this tile is a starting position for a player.
     * 检查此板块是否为玩家的起始位置。
     * @return true if it's a starting tile, false otherwise. (如果是起始板块则为 true，否则为 false)
     */
    public boolean isStartingTileForPlayer() {
        return isStartingTileForPlayer;
    }

    /**
     * Sets whether this tile is a starting position for a player.
     * 设置此板块是否为玩家的起始位置。
     * @param startingTileForPlayer true if it's a starting tile, false otherwise. (如果是起始板块则为 true，否则为 false)
     */
    public void setStartingTileForPlayer(boolean startingTileForPlayer) {
        this.isStartingTileForPlayer = startingTileForPlayer;
    }

    // It would be good to have an Enum for TreasureType later
    // 稍后最好为 TreasureType 创建一个枚举
    // For now, we can assume it's a String or a dedicated class
    // 目前，我们可以假设它是一个字符串或一个专门的类
} 