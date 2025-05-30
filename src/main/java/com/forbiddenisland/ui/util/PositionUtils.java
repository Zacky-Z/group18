package com.forbiddenisland.ui.util;

import com.forbiddenisland.core.model.IslandTile;

import java.util.ArrayList;
import java.util.List;

public class PositionUtils {
    /**
     * Get adjacent tiles for a given tile on the board
     * @param tile The target tile
     * @param allTiles All tiles on the game board
     * @return List of adjacent (non-sunken) tiles
     */
    public static List<IslandTile> getAdjacentTiles(IslandTile tile, List<IslandTile> allTiles) {
        List<IslandTile> adjacent = new ArrayList<>();
        int x = tile.getX();
        int y = tile.getY();

        // Check all four directions (up, down, left, right)
        checkAdjacentTile(x + 1, y, allTiles, adjacent);
        checkAdjacentTile(x - 1, y, allTiles, adjacent);
        checkAdjacentTile(x, y + 1, allTiles, adjacent);
        checkAdjacentTile(x, y - 1, allTiles, adjacent);

        return adjacent;
    }

    /**
     * Check if there's a valid non-sunken tile at given coordinates and add to result if found
     */
    private static void checkAdjacentTile(int x, int y, List<IslandTile> allTiles, List<IslandTile> result) {
        for (IslandTile t : allTiles) {
            if (t.getX() == x && t.getY() == y && !t.isSunk()) {
                result.add(t);
                break;
            }
        }
    }

    /**
     * Calculate Manhattan distance between two tiles
     */
    public static int manhattanDistance(IslandTile tile1, IslandTile tile2) {
        return Math.abs(tile1.getX() - tile2.getX()) + Math.abs(tile1.getY() - tile2.getY());
    }
}