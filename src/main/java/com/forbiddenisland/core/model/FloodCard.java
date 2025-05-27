package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.TileName;

public class FloodCard extends Card {
    private final TileName tileName;

    public FloodCard(TileName tileName) {
        super(tileName.getDisplayName() + "洪水卡");
        this.tileName = tileName;
    }

    public TileName getTileName() {
        return tileName;
    }

    @Override
    public String getDescription() {
        return "淹没" + tileName.getDisplayName() + "的洪水卡";
    }
}    