package com.forbiddenisland.model;

// It might be good to use JavaFX Color later for GUI integration.
// 稍后为了GUI集成，使用JavaFX的Color类可能会更好。
// For now, a simple String or an Enum for color would suffice.
// 目前，一个简单的字符串或者颜色枚举就足够了。

/**
 * Represents a player's pawn in the game.
 * 代表游戏中的玩家棋子。
 */
public class Pawn {
    private String color; // Color of the pawn (棋子的颜色)
    private IslandTile currentLocation; // Current location of the pawn on an IslandTile (棋子在岛屿板块上的当前位置)

    /**
     * Constructor for Pawn.
     * Pawn 的构造函数。
     * @param color The color of the pawn. (棋子的颜色)
     * @param startingLocation The initial IslandTile location of the pawn. (棋子的初始岛屿板块位置)
     */
    public Pawn(String color, IslandTile startingLocation) {
        this.color = color;
        this.currentLocation = startingLocation;
    }

    /**
     * Gets the color of the pawn.
     * 获取棋子的颜色。
     * @return The color string. (颜色字符串)
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the current location (IslandTile) of the pawn.
     * 获取棋子的当前位置（IslandTile）。
     * @return The IslandTile where the pawn is located. (棋子所在的IslandTile)
     */
    public IslandTile getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Sets the current location (IslandTile) of the pawn.
     * 设置棋子的当前位置（IslandTile）。
     * @param newLocation The new IslandTile for the pawn. (棋子的新IslandTile)
     */
    public void setCurrentLocation(IslandTile newLocation) {
        this.currentLocation = newLocation;
    }

    /**
     * Moves the pawn to a new IslandTile.
     * 将棋子移动到新的IslandTile。
     * This is a basic move; special moves (Pilot, Diver) will be handled by game logic considering AdventurerRole.
     * 这是基本移动；特殊移动（飞行员、潜水员）将由游戏逻辑结合探险家角色处理。
     * @param targetTile The IslandTile to move to. (要移动到的IslandTile)
     */
    public void moveTo(IslandTile targetTile) {
        // Basic validation can be added here, e.g., if targetTile is null
        // 可以在此处添加基本验证，例如 targetTile 是否为 null
        if (targetTile != null) {
            this.currentLocation = targetTile;
        }
    }
} 