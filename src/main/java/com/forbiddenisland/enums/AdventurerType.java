package com.forbiddenisland.enums;

public enum AdventurerType {
    PILOT("Pilot"),
    ENGINEER("Engineer"),
    MESSENGER("Messenger"),
    DIVER("Diver"),
    EXPLORER("Explorer"),
    NAVIGATOR("Navigator");

    private final String displayName;

    AdventurerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}