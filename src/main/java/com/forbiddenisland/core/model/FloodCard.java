package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.TileName;

/**
 * Represents a Flood Card in the Forbidden Island game.
 * Each flood card is associated with a specific tile on the island.
 */
public class FloodCard extends Card {
    private final TileName tileName;

    /**
     * Constructs a new FloodCard for the given tile.
     *
     * @param tileName the name of the tile this flood card is associated with
     */
    public FloodCard(TileName tileName) {
        super(tileName.getDisplayName() + " Flood Card");
        this.tileName = tileName;
    }

    /**
     * Returns the tile name associated with this flood card.
     *
     * @return the tile name
     */
    public TileName getTileName() {
        return tileName;
    }

    /**
     * Returns a description of what this flood card does.
     *
     * @return a description string
     */
    @Override
    public String getDescription() {
        return "Flood card for " + tileName.getDisplayName();
    }
}
