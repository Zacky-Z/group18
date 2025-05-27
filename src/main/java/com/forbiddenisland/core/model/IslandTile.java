package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TileState;
import com.forbiddenisland.enums.TreasureType;

public class IslandTile {
    private final TileName name;
    private TileState state;
    private TreasureType treasure;
    private int x;
    private int y;
    private boolean hasHelipad;

    public IslandTile(TileName name, int x, int y) {
        this.name = name;
        this.state = TileState.NORMAL;
        this.x = x;
        this.y = y;
    }

    // Getters and setters
    public TileName getName() { return name; }
    public TileState getState() { return state; }
    public void setState(TileState state) { this.state = state; }
    public TreasureType getTreasure() { return treasure; }
    public void setTreasure(TreasureType treasure) { this.treasure = treasure; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean hasHelipad() { return hasHelipad; }
    public void setHasHelipad(boolean hasHelipad) { this.hasHelipad = hasHelipad; }

    // Business methods
    public void flood() {
        if (state == TileState.NORMAL) {
            state = TileState.FLOODED;
        } else if (state == TileState.FLOODED) {
            state = TileState.SUNK;
        }
    }

    public boolean shoreUp() {
        if (state == TileState.FLOODED) {
            state = TileState.NORMAL;
            return true;
        }
        return false;
    }

    public boolean isSunk() {
        return state == TileState.SUNK;
    }

    public boolean isFlooded() {
        return state == TileState.FLOODED;
    }
}    