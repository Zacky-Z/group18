package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.TreasureType;

public class TreasureCard extends Card {
    private final TreasureType treasureType;

    public TreasureCard(TreasureType treasureType) {
        super(treasureType.getDisplayName() + "卡");
        this.treasureType = treasureType;
    }

    public TreasureType getTreasureType() {
        return treasureType;
    }

    @Override
    public String getDescription() {
        return "获取" + treasureType.getDisplayName() + "的必要卡牌";
    }
}    