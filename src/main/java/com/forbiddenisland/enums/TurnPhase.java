package com.forbiddenisland.enums;

public enum TurnPhase {
    ACTION("行动阶段"),
    TREASURE_CARD_DRAW("抽取宝物卡阶段"),
    FLOOD("洪水阶段");

    private final String displayName;

    TurnPhase(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}    