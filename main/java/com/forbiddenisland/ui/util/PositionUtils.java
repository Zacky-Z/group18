package com.forbiddenisland.ui.util;

import com.forbiddenisland.core.model.IslandTile;

import java.util.ArrayList;
import java.util.List;

public class PositionUtils {
    public static List<IslandTile> getAdjacentTiles(IslandTile tile, List<IslandTile> allTiles) {
        List<IslandTile> adjacent = new ArrayList<>();
        int x = tile.getX();
        int y = tile.getY();

        // 检查上下左右四个方向
        checkAdjacentTile(x + 1, y, allTiles, adjacent);
        checkAdjacentTile(x - 1, y, allTiles, adjacent);
        checkAdjacentTile(x, y + 1, allTiles, adjacent);
        checkAdjacentTile(x, y - 1, allTiles, adjacent);

        return adjacent;
    }

    private static void checkAdjacentTile(int x, int y, List<IslandTile> allTiles, List<IslandTile> result) {
        for (IslandTile t : allTiles) {
            if (t.getX() == x && t.getY() == y && !t.isSunk()) {
                result.add(t);
                break;
            }
        }
    }

    public static int manhattanDistance(IslandTile tile1, IslandTile tile2) {
        return Math.abs(tile1.getX() - tile2.getX()) + Math.abs(tile1.getY() - tile2.getY());
    }
}    