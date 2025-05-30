package com.forbiddenisland.model;

/**
 * Enum representing the different types of treasures in the game.
 */
public enum TreasureType {
    THE_EARTH_STONE("Earth Stone"),
    THE_STATUE_OF_THE_WIND("Statue of the Wind"),
    THE_CRYSTAL_OF_FIRE("Crystal of Fire"),
    THE_OCEANS_CHALICE("Ocean's Chalice");

    private final String displayName;

    TreasureType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // toString() could also be overridden if the enum constant name itself is not desired for general use.
    // @Override
    // public String toString() {
    //     return displayName;
    // }
}