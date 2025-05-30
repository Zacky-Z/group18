package com.forbiddenisland.core.system;

import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TreasureType;

import java.util.*;

public class IslandGenerator {
    private static final int MAP_SIZE = 6;
    private final Random random;

    public IslandGenerator() {
        this.random = new Random();
    }

    public List<IslandTile> generateStandardMap() {
        List<IslandTile> tiles = new ArrayList<>();
        initializeEmptyGrid(tiles);
        placeTreasureTiles(tiles);
        placeHelipad(tiles);
        return tiles;
    }

    private void initializeEmptyGrid(List<IslandTile> tiles) {
        // Logic to initialize the grid
    }

    private void placeTreasureTiles(List<IslandTile> tiles) {
        // Logic to place treasure tiles
    }

    private void placeHelipad(List<IslandTile> tiles) {
        // Logic to place the helipad
    }

    private IslandTile findTileByName(List<IslandTile> tiles, TileName name) {
        return tiles.stream()
                .filter(t -> t.getName() == name)
                .findFirst()
                .orElse(null);
    }
}