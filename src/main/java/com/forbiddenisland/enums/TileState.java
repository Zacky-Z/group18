package com.forbiddenisland.enums;

public enum TileState {
    NORMAL("Normal"),
    FLOODED("Flooded"),
    SUNK("Sunk");

    private final String displayName;

    TileState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}