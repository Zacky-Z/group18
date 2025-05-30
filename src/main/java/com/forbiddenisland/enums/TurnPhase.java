package com.forbiddenisland.enums;

public enum TurnPhase {
    ACTION("Action Phase"),
    TREASURE_CARD_DRAW("Treasure Card Draw Phase"),
    FLOOD("Flood Phase");

    private final String displayName;

    TurnPhase(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}