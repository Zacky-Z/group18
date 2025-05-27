package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.TreasureType;

public class Treasure {
    private final TreasureType type;
    private boolean captured;

    public Treasure(TreasureType type) {
        this.type = type;
        this.captured = false;
    }

    public TreasureType getType() {
        return type;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void capture() {
        this.captured = true;
    }
}    