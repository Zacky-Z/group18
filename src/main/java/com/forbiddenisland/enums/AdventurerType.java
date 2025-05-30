package com.forbiddenisland.enums;

/**
 * Enum representing the different types of adventurers in the game.
 * Each adventurer has unique abilities and a display name.
 */
public enum AdventurerType {
    PILOT("Pilot"),
    ENGINEER("Engineer"),
    MESSENGER("Messenger"),
    DIVER("Diver"),
    EXPLORER("Explorer"),
    NAVIGATOR("Navigator");

    private final String displayName;

    /**
     * Constructor for AdventurerType.
     * @param displayName The display name of the adventurer
     */
    AdventurerType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the adventurer.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}    