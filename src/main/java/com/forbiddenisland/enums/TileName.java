package com.forbiddenisland.enums;

/**
 * Enum representing the different tiles in the game.
 * Each tile has a display name that represents its location on the island.
 */
public enum TileName {
    CRYSTAL_CAVE("Crystal Cave"),
    CORAL_PALACE("Coral Palace"),
    TEMPLE_OF_THE_SUN("Temple of the Sun"),
    TEMPLE_OF_THE_MOON("Temple of the Moon"),
    IRON_ANVIL_ROCK("Iron Anvil Rock"),
    CLIFF_OF_AGES("Cliff of Ages"),
    OBSERVATORY("Observatory"),
    PHANTOM_ROCK("Phantom Rock"),
    BREAKERS_BRIDGE("Breakers Bridge"),
    DUNES_OF_DECEPTION("Dunes of Deception"),
    HOWLING_GARDEN("Howling Garden"),
    LOST_LAGOON("Lost Lagoon"),
    MISTY_MARSH("Misty Marsh"),
    SHADOW_WOODS("Shadow Woods"),
    WHISPERING_GARDEN("Whispering Garden"),
    CAVE_OF_EMBERS("Cave of Embers"),
    TWILIGHT_HOLLOW("Twilight Hollow"),
    TIDAL_PALACE("Tidal Palace"),
    WATCHTOWER("Watchtower"),
    BAY_OF_SILVER("Bay of Silver"),
    GOLD_GATE("Gold Gate");

    private final String displayName;

    /**
     * Constructor for TileName.
     * @param displayName The display name of the tile
     */
    TileName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the tile.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}    