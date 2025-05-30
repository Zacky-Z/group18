package com.forbiddenisland.model;


import java.io.Serializable;
/**
 * Represents an island tile in the game.
 * 代表游戏中的一个岛屿板块。
 */
public class IslandTile  implements Serializable{

    private String name; // Name of the island tile (板块名称)
    private boolean flooded; // Whether the tile is flooded (板块是否被淹没)
    private TreasureType associatedTreasure; // Treasure associated with this tile, if any (与此板块关联的宝藏，如果有)
    private boolean isStartingTileForPlayer; // Whether this tile is a starting position for a player (此板块是否为玩家的起始位置)
    private boolean sunk; // Whether the tile is sunk (completely removed from the game)

    /**
     * Constructor for IslandTile.
     * IslandTile 的构造函数。
     * @param name The name of the island tile. (岛屿板块的名称)
     */
    public IslandTile(String name) {
        this.name = name;
        this.flooded = false; // Tiles start unflooded (板块初始未被淹没)
        this.associatedTreasure = null; // No treasure by default (默认没有宝藏)
        this.isStartingTileForPlayer = false; // Not a starting tile by default (默认不是起始板块)
        this.sunk = false; // Tiles are not sunk by default
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
     * Gets the current flood state of the tile.
     * 获取板块当前的淹没状态。
     * @return true if the tile is flooded, false otherwise. (如果板块已淹没则为 true，否则为 false)
     */
    public boolean isFlooded() {
        return flooded;
    }

    /**
     * Checks if the tile is sunk (completely removed from the game).
     * 检查板块是否已沉没（从游戏中完全移除）。
     * @return true if the tile is sunk, false otherwise.
     */
    public boolean isSunk() {
        return sunk;
    }

    /**
     * Sets whether the tile is sunk.
     * 设置板块是否已沉没。
     * @param sunk true if the tile should be marked as sunk, false otherwise.
     */
    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }

    /**
     * Sets the flooded state of the tile.
     * 设置板块的淹没状态。
     * @param flooded The new flooded state. (新的淹没状态)
     */
    public void setFlooded(boolean flooded) {
        this.flooded = flooded;
    }

    /**
     * Flips the tile to its flooded side.
     * 将板块翻到其淹没的一面。
     */
    public void flood() {
        this.flooded = true;
    }

    /**
     * Shores up this tile, changing it from flooded to normal state.
     * 治水这个板块，将其从淹没状态恢复到正常状态。
     * @return true if the tile was successfully shored up, false if it was not flooded.
     */
    public boolean shoreUp() {
        if (flooded) {
            flooded = false;
            System.out.println("Tile " + name + " has been shored up. (板块 " + name + " 已被治水。)");
            return true;
        }
        System.out.println("Tile " + name + " was not flooded, cannot shore up. (板块 " + name + " 未被淹没，无法治水。)");
        return false;
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