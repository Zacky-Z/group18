package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.TreasureType;

public class TreasureCard extends Card {
    private final TreasureType treasureType;

    public TreasureCard(TreasureType treasureType) {
        super(treasureType != null ? treasureType.getDisplayName() + " Card" : "Special Card");
        this.treasureType = treasureType;
    }

    public TreasureType getTreasureType() {
        return treasureType;
    }

    @Override
    public String getDescription() {
        return treasureType != null ? "A necessary card to collect the " + treasureType.getDisplayName() : "Special card";
    }
}
