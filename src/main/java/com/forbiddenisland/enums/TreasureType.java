package com.forbiddenisland.enums;

public enum TreasureType {
    THE_EARTH_STONE("地球石", TileName.CRYSTAL_CAVE),
    THE_STATUE_OF_THE_WIND("风神雕像", TileName.CORAL_PALACE),
    THE_CRYSTAL_OF_FIRE("火焰水晶", TileName.TEMPLE_OF_THE_SUN),
    THE_OCEANS_CHALICE("海洋圣杯", TileName.TEMPLE_OF_THE_MOON);

    private final String displayName;
    private final TileName associatedTile;

    TreasureType(String displayName, TileName associatedTile) {
        this.displayName = displayName;
        this.associatedTile = associatedTile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TileName getAssociatedTile() {
        return associatedTile;
    }
}    