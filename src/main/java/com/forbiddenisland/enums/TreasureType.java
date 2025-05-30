package com.forbiddenisland.enums;

/**
 * Enum representing the different types of treasures in the game.
 * Each treasure has a display name and an associated tile where it can be found.
 */
public enum TreasureType {
    THE_EARTH_STONE("Earth Stone", TileName.CRYSTAL_CAVE),
    THE_STATUE_OF_THE_WIND("Wind Statue", TileName.CORAL_PALACE),
    THE_CRYSTAL_OF_FIRE("Fire Crystal", TileName.TEMPLE_OF_THE_SUN),
    THE_OCEANS_CHALICE("Ocean Chalice", TileName.TEMPLE_OF_THE_MOON);

    private final String displayName;
    private final TileName associatedTile;

    /**
     * Constructor for TreasureType.
     * @param displayName The display name of the treasure
     * @param associatedTile The tile associated with this treasure
     */
    TreasureType(String displayName, TileName associatedTile) {
        this.displayName = displayName;
        this.associatedTile = associatedTile;
    }

    /**
     * Gets the display name of the treasure.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the tile associated with this treasure.
     * @return The associated tile
     */
    public TileName getAssociatedTile() {
        return associatedTile;
    }
}    