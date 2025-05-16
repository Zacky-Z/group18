package com.forbiddenisland.enums;

public enum SpecialCardType {
    HELICOPTER_LIFT("Helicopter Lift", "Move any number of pawns to any tile."),
    SANDBAGS("Sandbags", "Shore up any one tile."),
    WATERS_RISE("Waters Rise", "Increase water level and reshuffle flood discard pile into flood deck.");

    private final String displayName;
    private final String description;

    SpecialCardType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}    