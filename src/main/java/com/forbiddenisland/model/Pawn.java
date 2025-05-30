package com.forbiddenisland.model;
import java.io.Serializable;

// Integration note: Will use JavaFX Color for GUI implementation in Week 12
// GUI集成说明：将在第12周使用JavaFX的Color类实现

/**
 * Represents a player's pawn in the game.
 * Core component for player movement and position tracking (Week 11).
 * 代表游戏中的玩家棋子。
 */
public class Pawn implements Serializable{
    private String color; // Visual identifier for the pawn in the GUI (棋子的颜色)
    private IslandTile currentLocation; // Current tile position on the game board (棋子在岛屿板块上的当前位置)

    /**
     * Constructor for Pawn.
     * Initializes a new pawn with a color and starting position.
     * @param color The color identifier for the pawn
     * @param startingLocation The initial tile position
     */
    public Pawn(String color, IslandTile startingLocation) {
        this.color = color;
        this.currentLocation = startingLocation;
    }

    /**
     * Gets the pawn's color identifier.
     * Used for GUI representation and player identification.
     * @return The color string
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the current tile position of the pawn.
     * Essential for movement validation and game state tracking.
     * @return The IslandTile where the pawn is located
     */
    public IslandTile getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Updates the pawn's position on the board.
     * Core method for implementing movement mechanics.
     * @param newLocation The new tile position for the pawn
     */
    public void setCurrentLocation(IslandTile newLocation) {
        this.currentLocation = newLocation;
    }

    /**
     * Moves the pawn to a new tile position.
     * Basic movement implementation - special moves are handled by game logic.
     * Part of the movement mechanics implementation (Week 11).
     * @param targetTile The destination tile
     */
    public void moveTo(IslandTile targetTile) {
        if (targetTile != null) {
            this.currentLocation = targetTile;
        }
    }
} 