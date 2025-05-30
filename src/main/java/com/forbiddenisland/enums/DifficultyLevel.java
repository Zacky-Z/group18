package com.forbiddenisland.enums;

public enum DifficultyLevel {
    NOVICE("Novice", 1),
    NORMAL("Normal", 2),
    ELITE("Elite", 3),
    LEGENDARY("Legendary", 5);

    private final String displayName;
    private final int initialWaterLevel;

    DifficultyLevel(String displayName, int initialWaterLevel) {
        this.displayName = displayName;
        this.initialWaterLevel = initialWaterLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getInitialWaterLevel() {
        return initialWaterLevel;
    }
}