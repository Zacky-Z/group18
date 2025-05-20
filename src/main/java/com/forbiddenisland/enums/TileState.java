package com.forbiddenisland.enums;

public enum TileState {
    NORMAL("正常"),
    FLOODED("洪水"),
    SUNK("沉没");

    private final String displayName;

    TileState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}    