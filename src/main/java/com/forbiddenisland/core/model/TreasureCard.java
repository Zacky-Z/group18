package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.TreasureType;

public class TreasureCard extends Card {
    private final TreasureType treasureType;

    public TreasureCard(TreasureType treasureType) {
        super(treasureType != null ? treasureType.getDisplayName() + "卡" : "特殊卡");
        this.treasureType = treasureType;
    }

    public TreasureType getTreasureType() {
        return treasureType;
    }

    @Override
    public String getDescription() {
        return treasureType != null ? "获取" + treasureType.getDisplayName() + "的必要卡牌" : "特殊卡牌";
    }
}    