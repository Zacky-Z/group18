package com.forbiddenisland.enums;

public enum DifficultyLevel {
    NOVICE("新手", 1),
    NORMAL("标准", 2),
    ELITE("精英", 3),
    LEGENDARY("传奇", 5);

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